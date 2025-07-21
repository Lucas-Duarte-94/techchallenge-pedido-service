package com.tech_challenge.fiap_pedido_service.core.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productSKU) {
        super("Produto com código " + productSKU + " não encontrado!");
    }
}
