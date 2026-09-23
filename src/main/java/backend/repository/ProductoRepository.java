package backend.repository;

import backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Spring Data JPA arma la consulta SQL automáticamente fijándose en el nombre del método
    List<Producto> findByActivoTrueAndStockGreaterThan(Integer stock);

    List<Producto> findByCategoriaIgnoreCaseAndActivoTrueAndStockGreaterThan(String categoria, Integer stock);
}