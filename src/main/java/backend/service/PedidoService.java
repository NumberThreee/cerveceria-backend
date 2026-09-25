package backend.service;

import backend.dto.CheckoutRequestDTO;
import backend.dto.CheckoutResponseDTO;
import backend.dto.ItemCheckoutDTO;
import backend.model.DetallePedido;
import backend.model.EstadoPedido;
import backend.model.Pedido;
import backend.model.Producto;
import backend.repository.PedidoRepository;
import backend.repository.ProductoRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    @Value("${mercadopago.access.token:TEST-TOKEN-MOCK}")
    private String mpAccessToken;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public CheckoutResponseDTO crearPedidoYPreferencia(CheckoutRequestDTO checkoutDTO) {
        // 1. Crear instancia de Pedido
        Pedido pedido = new Pedido();
        pedido.setClienteUuid(checkoutDTO.getClienteUuid());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        double montoTotal = 0.0;
        List<PreferenceItemRequest> itemsPreference = new ArrayList<>();

        // 2. Procesar ítems, calcular totales y armar detalles
        for (ItemCheckoutDTO itemDTO : checkoutDTO.getItems()) {
            Producto producto = productoRepository.findById(itemDTO.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + itemDTO.getIdProducto()));

            if (!producto.getActivo() || producto.getStock() < itemDTO.getCantidad()) {
                throw new RuntimeException("El producto " + producto.getNombre() + " no está disponible o no tiene stock suficiente.");
            }

            DetallePedido detalle = new DetallePedido(producto, itemDTO.getCantidad(), producto.getPrecio());
            pedido.agregarDetalle(detalle);

            montoTotal += producto.getPrecio() * itemDTO.getCantidad();

            // Ítem para la preferencia de Mercado Pago
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(producto.getNombre())
                    .quantity(itemDTO.getCantidad())
                    .unitPrice(BigDecimal.valueOf(producto.getPrecio()))
                    .currencyId("ARS")
                    .build();
            itemsPreference.add(itemRequest);
        }

        pedido.setMontoTotal(montoTotal);

        // 3. Guardar pedido en base de datos
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 4. Intentar generar preferencia en Mercado Pago
        String initPoint = "";
        try {
            if (!"TEST-TOKEN-MOCK".equals(mpAccessToken)) {
                MercadoPagoConfig.setAccessToken(mpAccessToken);

                PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                        .items(itemsPreference)
                        .externalReference(pedidoGuardado.getId().toString())
                        .build();

                PreferenceClient client = new PreferenceClient();
                Preference preference = client.create(preferenceRequest);

                initPoint = preference.getInitPoint(); // URL de Checkout MP
                pedidoGuardado.setMpPreferenceId(preference.getId());
                pedidoRepository.save(pedidoGuardado);
            } else {
                // Modo Simulación/Prueba si no se configuró un Access Token real aún
                initPoint = "https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=mock_preference_" + pedidoGuardado.getId();
            }
        } catch (Exception e) {
            // Si ocurre un error con la API de MP, dejamos la URL mock para no bloquear las pruebas
            initPoint = "https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=mock_preference_" + pedidoGuardado.getId();
        }

        return new CheckoutResponseDTO(pedidoGuardado.getId(), initPoint);
    }

    @Transactional
    public void procesarNotificacionPago(String paymentId) {
        try {
            if (!"TEST-TOKEN-MOCK".equals(mpAccessToken)) {
                MercadoPagoConfig.setAccessToken(mpAccessToken);
                com.mercadopago.client.payment.PaymentClient paymentClient = new com.mercadopago.client.payment.PaymentClient();
                com.mercadopago.resources.payment.Payment payment = paymentClient.get(Long.parseLong(paymentId));

                String estadoPago = payment.getStatus();
                String externalReference = payment.getExternalReference();

                if (externalReference == null) {
                    return;
                }

                Long pedidoId = Long.parseLong(externalReference);

                if ("approved".equals(estadoPago)) {
                    actualizarEstadoAPagado(pedidoId);
                } else if ("rejected".equals(estadoPago) || "cancelled".equals(estadoPago)) {
                    // NUEVO (25/09): antes esto no hacía nada y el pedido quedaba
                    // PENDIENTE para siempre. Ahora lo marcamos RECHAZADO para que
                    // la app del mozo pueda distinguir "rechazado" de "todavía sin
                    // confirmar".
                    actualizarEstadoARechazado(pedidoId);
                }
                // Otros estados de Mercado Pago (pending, in_process, etc.) no
                // cambian el pedido: sigue PENDIENTE hasta la próxima notificación.
            } else {
                // Modo simulación local.
                // Prefijo "mock_rechazado_<id>" simula un pago rechazado (para poder
                // probar ese camino sin depender de Mercado Pago real). Se revisa
                // ANTES que "mock_" a secas, porque también empieza con ese prefijo.
                if (paymentId != null && paymentId.startsWith("mock_rechazado_")) {
                    Long pedidoId = Long.parseLong(paymentId.replace("mock_rechazado_", ""));
                    actualizarEstadoARechazado(pedidoId);
                } else if (paymentId != null && paymentId.startsWith("mock_")) {
                    Long pedidoId = Long.parseLong(paymentId.replace("mock_", ""));
                    actualizarEstadoAPagado(pedidoId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error procesando webhook de Mercado Pago: " + e.getMessage());
        }
    }

    private void actualizarEstadoAPagado(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido != null && pedido.getEstado() == EstadoPedido.PENDIENTE) {
            pedido.setEstado(EstadoPedido.PAGADO);

            // Descontar stock de los productos
            for (DetallePedido detalle : pedido.getDetalles()) {
                Producto producto = detalle.getProducto();
                int nuevoStock = producto.getStock() - detalle.getCantidad();
                producto.setStock(Math.max(nuevoStock, 0));
                productoRepository.save(producto);
            }

            pedidoRepository.save(pedido);
        }
    }

    // NUEVO (25/09): marca un pedido como RECHAZADO cuando Mercado Pago
    // devuelve el pago como "rejected"/"cancelled". Solo lo hace si el pedido
    // sigue PENDIENTE (si ya estaba PAGADO por otra notificación previa, no
    // lo pisamos con un rechazo tardío/duplicado).
    private void actualizarEstadoARechazado(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido != null && pedido.getEstado() == EstadoPedido.PENDIENTE) {
            pedido.setEstado(EstadoPedido.RECHAZADO);
            pedidoRepository.save(pedido);
        }
    }

    public List<Pedido> obtenerPedidosPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    // NUEVO (25/09): busca UN pedido puntual por id, sin filtrar por estado.
    // Lo usa el endpoint GET /api/empleado/pedidos/{id} para cuando el mozo
    // escanea el QR de compra del cliente.
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido entregarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getEstado() != EstadoPedido.PAGADO) {
            throw new RuntimeException("Solo se pueden entregar pedidos que estén en estado PAGADO");
        }

        pedido.setEstado(EstadoPedido.ENTREGADO);
        return pedidoRepository.save(pedido);
    }
}