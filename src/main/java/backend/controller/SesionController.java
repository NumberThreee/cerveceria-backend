package backend.controller;

import backend.dto.SesionRequest;
import backend.dto.SesionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sesiones")
@CrossOrigin(origins = "*")
public class SesionController {

    @PostMapping("/anonima")
    public ResponseEntity<SesionResponse> iniciarSesionAnonima(@RequestBody SesionRequest request) {
        String nuevoUuid = UUID.randomUUID().toString();
        Integer numeroMesa = (request != null) ? request.getNumeroMesa() : null;

        SesionResponse respuesta = new SesionResponse(nuevoUuid, numeroMesa);
        return ResponseEntity.ok(respuesta);
    }
}