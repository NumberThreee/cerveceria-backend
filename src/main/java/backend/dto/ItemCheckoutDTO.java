package backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemCheckoutDTO {
    @JsonProperty("id_producto")
    private Long idProducto;
    private Integer cantidad;

    public ItemCheckoutDTO() {}

    public ItemCheckoutDTO(Long idProducto, Integer cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}