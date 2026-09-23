package backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SesionResponse {

    @JsonProperty("cliente_uuid")
    private String clienteUuid;

    @JsonProperty("numero_mesa")
    private Integer numeroMesa;

    public SesionResponse(String clienteUuid, Integer numeroMesa) {
        this.clienteUuid = clienteUuid;
        this.numeroMesa = numeroMesa;
    }

    public String getClienteUuid() {
        return clienteUuid;
    }

    public void setClienteUuid(String clienteUuid) {
        this.clienteUuid = clienteUuid;
    }

    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(Integer numeroMesa) {
        this.numeroMesa = numeroMesa;
    }
}