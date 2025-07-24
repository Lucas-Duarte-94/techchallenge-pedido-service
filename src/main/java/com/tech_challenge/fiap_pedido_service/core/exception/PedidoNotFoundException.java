package com.tech_challenge.fiap_pedido_service.core.exception;

public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException(String msg) {
        super(msg);
    }

    public PedidoNotFoundException() {
        super("Pedido não encontrado.");
    }
}
