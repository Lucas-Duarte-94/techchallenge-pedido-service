package com.tech_challenge.fiap_pedido_service.core.gateway;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;
import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;

public interface PedidoGateway {
    void createPedido(CreatePedidoDTO pedidioDTO);

    void changePedidoStatus(Pedido pedido);
}