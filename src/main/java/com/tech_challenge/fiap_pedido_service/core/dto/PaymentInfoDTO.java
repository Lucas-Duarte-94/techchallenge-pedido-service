package com.tech_challenge.fiap_pedido_service.core.dto;

import java.math.BigDecimal;

public record PaymentInfoDTO(
        String pedidoId,
        BigDecimal valor,
        PaymentMethod metodoPagamento,
        CreditCardRequestDTO creditCard) {

}
