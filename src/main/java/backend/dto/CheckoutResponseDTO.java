package backend.dto;

public class CheckoutResponseDTO {
    private Long idPedido;
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
