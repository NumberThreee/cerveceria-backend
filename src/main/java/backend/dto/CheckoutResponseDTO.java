package backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckoutResponseDTO {
    @JsonProperty("id_pedido") 
    private Long idPedido;
    @JsonProperty("init_point") 
    private String initPoint; // URL de Mercado Pago a la que redirecciona la App

    public CheckoutResponseDTO(Long idPedido, String initPoint) {
        this.idPedido = idPedido;
        this.initPoint = initPoint;
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public String getInitPoint() { return initPoint; }
    public void setInitPoint(String initPoint) { this.initPoint = initPoint; }
}
