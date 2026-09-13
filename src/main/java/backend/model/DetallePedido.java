package backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;
    private Double precioUnitarioHistorico;

    public DetallePedido() {}

    public DetallePedido(Producto producto, Integer cantidad, Double precioUnitarioHistorico) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitarioHistorico = precioUnitarioHistorico;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecioUnitarioHistorico() { return precioUnitarioHistorico; }
    public void setPrecioUnitarioHistorico(Double precioUnitarioHistorico) { this.precioUnitarioHistorico = precioUnitarioHistorico; }
}
