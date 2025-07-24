package com.tech_challenge.fiap_pedido_service.core.usecase;

public interface PedidoExpiredUseCase {
    void changeToClosedExpired(String pedidoId);
}
