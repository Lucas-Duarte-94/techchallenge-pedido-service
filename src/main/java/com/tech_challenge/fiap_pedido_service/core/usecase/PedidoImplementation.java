package com.tech_challenge.fiap_pedido_service.core.usecase;

import org.springframework.stereotype.Service;

import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.EstoqueRequestDTO;
import com.tech_challenge.fiap_pedido_service.core.gateway.PedidoGateway;

@Service
public class PedidoImplementation implements PedidoGateway {
    private CreatePedidoUseCase createPedidoUseCase;
    private PedidoExpiredUseCase pedidoExpiredUseCase;
    private PedidoOutOfStockUseCase pedidoOutOfStockUseCase;
    private PedidoPaymentFailUseCase pedidoPaymentFailUseCase;
    private PedidoSuccessUseCase pedidoSuccessUseCase;

    public PedidoImplementation(CreatePedidoUseCase createPedidoUseCase,
            PedidoExpiredUseCase pedidoExpiredUseCase,
            PedidoOutOfStockUseCase pedidoOutOfStockUseCase,
            PedidoPaymentFailUseCase pedidoPaymentFailUseCase,
            PedidoSuccessUseCase pedidoSuccessUseCase) {
        this.createPedidoUseCase = createPedidoUseCase;
    }

    @Override
    public void createPedido(CreatePedidoDTO pedidioDTO) {
        this.createPedidoUseCase.create(pedidioDTO);
    }

    @Override
    public void changeToClosedOutOfStock(EstoqueRequestDTO request) {
        this.pedidoOutOfStockUseCase.changeToClosedOutOfStock(request.pedidoId());
    }

    @Override
    public void changeToClosedPaymentFail(EstoqueRequestDTO request) {
        this.pedidoPaymentFailUseCase.changeToClosedPaymentFail(request.pedidoId());
    }

    @Override
    public void changeToClosedSuccess(EstoqueRequestDTO request) {
        this.pedidoSuccessUseCase.changeToClosedSuccess(request.pedidoId());
    }

    @Override
    public void changeToClosedExpired(EstoqueRequestDTO request) {
        this.pedidoExpiredUseCase.changeToClosedExpired(request.pedidoId());
    }

}
