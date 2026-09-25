package backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

// NUEVO (25/09): resuelve el problema del "QR de empleados" que cambiaba
// cada vez que se reiniciaba el túnel de Expo.
//
// Idea: en vez de que el QR apunte directo a la URL del túnel de Expo (que
// cambia todo el tiempo), el QR apunta a ESTE endpoint del backend, que es
// estable (usa el dominio fijo/estático de ngrok — el mismo de siempre,
// script-riverbank-helpful.ngrok-free.dev). Este endpoint simplemente
// redirige (302) a la URL de Expo que esté vigente en ese momento.
//
// Cuando reinician "npx expo start --tunnel" y cambia la URL, en vez de
// reimprimir el QR, se actualiza el destino con UNA sola llamada (ver el
// POST más abajo). El QR impreso no cambia nunca más.
//
// OJO: el destino se guarda en memoria. Si se reinicia el backend (no el
// túnel de Expo, el backend en sí), hay que volver a mandar el POST una vez
// más. Para un uso más permanente se podría guardar en un archivo o en la
// base, pero para esto alcanza y sobra.
@RestController
@RequestMapping("/api/empleado/acceso")
@CrossOrigin(origins = "*")
public class AccesoEmpleadoController {

    // TODO: cambiar este valor por algo propio antes de usarlo (no hace
    // falta que sea muy elaborado, solo que no sea adivinable al toque).
    private static final String CLAVE_ADMIN = "2026";

    // Valor por defecto: si todavía nadie configuró el destino, redirige a
    // una página de aviso en vez de a un link roto.
    private volatile String urlDestino = "https://expo.dev/go";

    // GET /api/empleado/acceso
    // Sin autenticación: es solo una redirección, no expone nada sensible.
    // Esto es lo que va DENTRO del QR que se imprime para los empleados.
    @GetMapping
    public ResponseEntity<Void> redirigir() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlDestino))
                .build();
    }

    // POST /api/empleado/acceso
    // Header requerido: X-Clave-Admin (ver CLAVE_ADMIN arriba)
    // Body: {"url": "exp://u-xxxx.anonymous.8081.exp.direct/--/empleado"}
    // Actualiza a dónde redirige el QR. Se llama una vez cada vez que se
    // reinicia el túnel de Expo y cambia la URL (no hace falta redeploy).
    @PostMapping
    public ResponseEntity<?> actualizarDestino(
            @RequestHeader("X-Clave-Admin") String clave,
            @RequestBody Map<String, String> body) {

        if (!CLAVE_ADMIN.equals(clave)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clave inválida");
        }

        String nuevaUrl = body.get("url");
        if (nuevaUrl == null || nuevaUrl.isBlank()) {
            return ResponseEntity.badRequest().body("Falta 'url' en el body");
        }

        this.urlDestino = nuevaUrl;
        return ResponseEntity.ok(Map.of("urlDestino", urlDestino));
    }
}
