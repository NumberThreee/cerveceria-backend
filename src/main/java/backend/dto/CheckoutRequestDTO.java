package backend.dto;

import java.util.List;

public class CheckoutRequestDTO {
    private String clienteUuid;
    private List<ItemCheckoutDTO> items;

    public CheckoutRequestDTO() {}

    public String getClienteUuid() { return clienteUuid; }
    public void setClienteUuid(String clienteUuid) { this.clienteUuid = clienteUuid; }

    public List<ItemCheckoutDTO> getItems() { return items; }
    public void setItems(List<ItemCheckoutDTO> items) { this.items = items; }
}
