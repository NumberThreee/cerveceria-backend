package backend.config;

import backend.model.Empleado;
import backend.model.Producto;
import backend.repository.EmpleadoRepository;
import backend.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final EmpleadoRepository empleadoRepository;

    public DataLoader(ProductoRepository productoRepository, EmpleadoRepository empleadoRepository) {
        this.productoRepository = productoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Productos de prueba
        productoRepository.save(new Producto("IPA Red", "Cerveza de amargor medio con notas cítricas", 4500.0, 50, "Cervezas", "", true));
        productoRepository.save(new Producto("Honey Ale", "Cerveza suave dulce sabor a miel", 4200.0, 30, "Cervezas", "", true));
        productoRepository.save(new Producto("Papas Rusticas", "Con cheddar, bacon y verdeo", 6500.0, 20, "Comida", "", true));
        
        // Empleado de prueba (para la barra)
        if (empleadoRepository.count() == 0) {
            empleadoRepository.save(new Empleado("Camarero Barra", "1234"));
        }
    }
}