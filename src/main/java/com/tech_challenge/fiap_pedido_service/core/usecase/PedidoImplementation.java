package com.tech_challenge.fiap_pedido_service.core.usecase;

import org.springframework.stereotype.Service;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;
import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.gateway.PedidoGateway;

@Service
public class PedidoImplementation implements PedidoGateway {
    private CreatePedidoUseCase createPedidoUseCase;

    public PedidoImplementation(CreatePedidoUseCase createPedidoUseCase) {
        this.createPedidoUseCase = createPedidoUseCase;
    }

    @Override
    public void createPedido(CreatePedidoDTO pedidioDTO) {
        this.createPedidoUseCase.create(pedidioDTO);
    }

    @Override
    public void changePedidoStatus(Pedido pedido) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePedidoStatus'");
    }

}
