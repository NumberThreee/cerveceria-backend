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

    // Listar pedidos listos para despachar (Estado: PAGADO)
    @GetMapping("/pedidos")
    public ResponseEntity<?> obtenerPedidosParaDespacho(@RequestHeader("X-Empleado-Pin") String pin) {
        if (!validarPin(pin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("PIN de empleado inválido");
        }
        
        List<Pedido> pedidosPagados = pedidoService.obtenerPedidosPorEstado(EstadoPedido.PAGADO);
        return ResponseEntity.ok(pedidosPagados);
    }

    // Marcar pedido como ENTREGADO
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