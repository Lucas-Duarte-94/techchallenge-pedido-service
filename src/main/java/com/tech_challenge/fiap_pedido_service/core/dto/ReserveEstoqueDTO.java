package com.tech_challenge.fiap_pedido_service.core.dto;

import java.util.List;

public record ReserveEstoqueDTO(
        List<ItemPedidoDTO> pedidos,
        String pedidoId) {

}
