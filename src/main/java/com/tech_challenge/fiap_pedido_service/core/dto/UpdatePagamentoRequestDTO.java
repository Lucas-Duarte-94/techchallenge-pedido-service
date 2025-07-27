package com.tech_challenge.fiap_pedido_service.core.dto;

public record UpdatePagamentoRequestDTO(
        String pedidoId,
        PaymentStatus status) {

}
