package com.tech_challenge.fiap_pedido_service.core.gateway;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;
import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.EstoqueRequestDTO;

public interface PedidoGateway {
    void createPedido(CreatePedidoDTO pedidioDTO);

    void changeToClosedOutOfStock(EstoqueRequestDTO request);

    void changeToClosedPaymentFail(EstoqueRequestDTO request);

    void changeToClosedSuccess(EstoqueRequestDTO request);

    void changeToClosedExpired(EstoqueRequestDTO request);
}