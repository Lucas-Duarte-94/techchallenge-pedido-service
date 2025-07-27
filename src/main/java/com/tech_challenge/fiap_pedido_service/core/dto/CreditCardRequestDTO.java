package com.tech_challenge.fiap_pedido_service.core.dto;

public record CreditCardRequestDTO(
        String creditCardNumber,
        String CVC,
        String ownerName,
        String validTo) {

}
