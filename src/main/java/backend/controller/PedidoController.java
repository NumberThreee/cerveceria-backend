package backend.controller;

import backend.dto.CheckoutRequestDTO;
import backend.dto.CheckoutResponseDTO;
import backend.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDTO> procesarCheckout(@RequestBody CheckoutRequestDTO checkoutDTO) {
        CheckoutResponseDTO response = pedidoService.crearPedidoYPreferencia(checkoutDTO);
        return ResponseEntity.ok(response);
    }
}