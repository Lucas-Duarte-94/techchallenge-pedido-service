package com.tech_challenge.fiap_pedido_service.core.dto;

public record EstoqueResponseDTO(
        String productSKU,
        int quantidadeDisponivel) {

}
