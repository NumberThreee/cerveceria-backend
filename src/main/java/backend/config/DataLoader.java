package backend.config;

import backend.model.Producto;
import backend.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    public DataLoader(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        productoRepository.save(new Producto("IPA Red", "Cerveza de amargor medio con notas cítricas", 4500.0, "Cervezas", "", true));
        productoRepository.save(new Producto("Honey Ale", "Cerveza suave dulce sabor a miel", 4200.0, "Cervezas", "", true));
        productoRepository.save(new Producto("Papas Rusticas", "Con cheddar, bacon y verdeo", 6500.0, "Comida", "", true));
    }
}
