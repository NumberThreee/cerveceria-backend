package backend.controller;

import backend.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoWebhookController {

    private final PedidoService pedidoService;

    public PagoWebhookController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> recibirNotificacionPago(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "data.id", required = false) String dataId,
            @RequestBody(required = false) Map<String, Object> payload) {

        try {
            String paymentId = null;

            // 1. Verificar si viene por query param (data.id)
            if ("payment".equals(type) && dataId != null && !dataId.isBlank()) {
                paymentId = dataId;
            } 
            // 2. Si no, verificar en el body JSON de forma segura (sin casteos riesgosos)
            else if (payload != null && "payment".equals(payload.get("type"))) {
                Object dataObj = payload.get("data");
                if (dataObj instanceof Map<?, ?> dataMap) {
                    Object idObj = dataMap.get("id");
                    if (idObj != null) {
                        paymentId = idObj.toString();
                    }
                }
            }

            // 3. Procesar si se obtuvo un ID válido
            if (paymentId != null) {
                pedidoService.procesarNotificacionPago(paymentId);
            }

        } catch (Exception e) {
            // Loguear el error sin cortar la respuesta HTTP
            System.err.println("Error procesando notificación de Mercado Pago: " + e.getMessage());
        }

        // Mercado Pago exige recibir 200 OK siempre.
        // Si respondemos con error, MP reintentará enviar la notificación decenas de veces.
        return ResponseEntity.ok().build();
    }
}