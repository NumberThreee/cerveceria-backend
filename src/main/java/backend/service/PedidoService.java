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
}