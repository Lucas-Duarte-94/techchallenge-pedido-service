package com.tech_challenge.fiap_pedido_service.core.dto;

import java.math.BigDecimal;

public record ItemPedidoDTO(
        String productSKU,
        int qtd,
        BigDecimal preco) {
}
