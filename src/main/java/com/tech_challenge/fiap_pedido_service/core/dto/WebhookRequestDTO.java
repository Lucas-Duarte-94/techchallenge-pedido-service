package com.tech_challenge.fiap_pedido_service.core.dto;

public record WebhookRequestDTO(
                String pedidoId,
                PaymentStatus status) {

}
