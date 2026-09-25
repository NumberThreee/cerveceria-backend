package backend.controller;

import backend.model.EstadoPedido;
import backend.model.Pedido;
import backend.repository.EmpleadoRepository;
import backend.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleado")
@CrossOrigin(origins = "*")
public class EmpleadoController {

    private final PedidoService pedidoService;
    private final EmpleadoRepository empleadoRepository;

    public EmpleadoController(PedidoService pedidoService, EmpleadoRepository empleadoRepository) {
        this.pedidoService = pedidoService;
        this.empleadoRepository = empleadoRepository;
    }

    // GET /api/empleado/pedidos
    // Header requerido: X-Empleado-Pin
    // Devuelve la lista de pedidos con estado PAGADO (esperando entrega).
    // La app de mozo llama a este endpoint apenas se ingresa la contraseña
    // (que es el mismo PIN de siempre), y también con el botón "Actualizar"
    // de la lista.
    @GetMapping("/pedidos")
    public ResponseEntity<?> obtenerPedidosParaDespacho(@RequestHeader("X-Empleado-Pin") String pin) {
        if (!validarPin(pin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("PIN de empleado inválido");
        }

        List<Pedido> pedidosPagados = pedidoService.obtenerPedidosPorEstado(EstadoPedido.PAGADO);
        return ResponseEntity.ok(pedidosPagados);
    }

    // NUEVO (25/09): GET /api/empleado/pedidos/{idPedido}
    // Header requerido: X-Empleado-Pin
    // Devuelve UN pedido puntual, en CUALQUIER estado (PENDIENTE, PAGADO,
    // RECHAZADO o ENTREGADO). Lo necesita la app cuando el mozo escanea el QR
    // de compra del cliente (que solo trae el id_pedido): hay que poder
    // consultar ese pedido puntual sin que dependa de que esté en estado
    // PAGADO, porque también hay que poder avisarle al mozo que el pago fue
    // rechazado, o que ese pedido ya se entregó antes (para que el QR sea de
    // un solo uso).
    //
    // Importante: a diferencia de "entregar" (que sí exige PAGADO), este GET
    // no filtra por estado — el propio Pedido.estado en la respuesta es lo
    // que la app usa para decidir qué mostrar.
    @GetMapping("/pedidos/{idPedido}")
    public ResponseEntity<?> obtenerPedidoPorId(
            @PathVariable Long idPedido,
            @RequestHeader("X-Empleado-Pin") String pin) {

        if (!validarPin(pin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("PIN de empleado inválido");
        }

        return pedidoService.obtenerPedidoPorId(idPedido)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado"));
    }

    // POST /api/empleado/pedidos/{idPedido}/entregar
    // Header requerido: X-Empleado-Pin
    // Marca el pedido como ENTREGADO. La app lo llama cuando el mozo toca
    // "Marcar como entregado" en el detalle del pedido (ya sea desde la
    // lista, o después de escanear el QR del cliente).
    @PostMapping("/pedidos/{idPedido}/entregar")
    public ResponseEntity<?> entregarPedido(
            @PathVariable Long idPedido,
            @RequestHeader("X-Empleado-Pin") String pin) {

        if (!validarPin(pin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("PIN de empleado inválido");
        }

        try {
            Pedido pedidoEntregado = pedidoService.entregarPedido(idPedido);
            return ResponseEntity.ok(pedidoEntregado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private boolean validarPin(String pin) {
        if (pin == null || pin.isBlank()) {
            return false;
        }
        return empleadoRepository.findByPin(pin).isPresent();
    }
}