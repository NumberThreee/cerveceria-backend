package backend.controller;

import backend.dto.SesionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sesiones")
@CrossOrigin(origins = "*")
public class SesionController {

    @PostMapping("/anonima")
    public ResponseEntity<SesionResponse> iniciarSesionAnonima() {
        String nuevoUuid = UUID.randomUUID().toString();
        SesionResponse respuesta = new SesionResponse(nuevoUuid, "Sesión iniciada correctamente");
        return ResponseEntity.ok(respuesta);
    }
}