package backend.dto;

public class SesionResponse {
    private String clienteUuid;
    private String mensaje;

    public SesionResponse(String clienteUuid, String mensaje) {
        this.clienteUuid = clienteUuid;
        this.mensaje = mensaje;
    }

    public String getClienteUuid() { return clienteUuid; }
    public void setClienteUuid(String clienteUuid) { this.clienteUuid = clienteUuid; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}