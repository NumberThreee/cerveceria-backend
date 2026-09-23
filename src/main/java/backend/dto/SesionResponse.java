package backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SesionResponse {

    @JsonProperty("cliente_uuid")
    private String clienteUuid;
    public SesionResponse() {
    }

    public SesionResponse(String clienteUuid) {
        this.clienteUuid = clienteUuid;
        
    }

    public String getClienteUuid() {
        return clienteUuid;
    }

    public void setClienteUuid(String clienteUuid) {
        this.clienteUuid = clienteUuid;
    }

}