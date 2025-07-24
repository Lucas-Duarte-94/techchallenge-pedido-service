package com.tech_challenge.fiap_pedido_service.core.usecase;

public interface PedidoOutOfStockUseCase {
    void changeToClosedOutOfStock(String pedidoId);
}
