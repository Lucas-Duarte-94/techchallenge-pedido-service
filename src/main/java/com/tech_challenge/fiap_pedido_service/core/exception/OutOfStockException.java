package com.tech_challenge.fiap_pedido_service.core.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String productSKU) {
        super("Estoque insuficiente para o produto: " + productSKU);
    }
}
