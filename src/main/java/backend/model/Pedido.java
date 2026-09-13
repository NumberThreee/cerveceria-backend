package backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora;
    private Double montoTotal;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    private String mpPreferenceId;
    private String mpPaymentId;
    private String clienteUuid;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
    }

    public Pedido(String clienteUuid, Double montoTotal) {
        this();
        this.clienteUuid = clienteUuid;
        this.montoTotal = montoTotal;
    }

    // Helper para agregar detalles manteniendo la relación bidireccional
    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(Double montoTotal) { this.montoTotal = montoTotal; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public String getMpPreferenceId() { return mpPreferenceId; }
    public void setMpPreferenceId(String mpPreferenceId) { this.mpPreferenceId = mpPreferenceId; }

    public String getMpPaymentId() { return mpPaymentId; }
    public void setMpPaymentId(String mpPaymentId) { this.mpPaymentId = mpPaymentId; }

    public String getClienteUuid() { return clienteUuid; }
    public void setClienteUuid(String clienteUuid) { this.clienteUuid = clienteUuid; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}