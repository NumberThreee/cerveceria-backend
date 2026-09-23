package backend.controller;

import backend.model.Producto;
import backend.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoRepository.findByActivoTrueAndStockGreaterThan(0);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable("id") Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cambiamos la ruta a /categoria/{categoria} para que no choque con /{id}
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable("categoria") String categoria) {
        List<Producto> productos = productoRepository
                .findByCategoriaIgnoreCaseAndActivoTrueAndStockGreaterThan(categoria, 0);

        if (productos.isEmpty()) {
            return ResponseEntity.notFound().build(); // o podés devolver ResponseEntity.ok(productos) si preferís lista vacía
        }

        return ResponseEntity.ok(productos);
    }
}