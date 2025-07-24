package com.tech_challenge.fiap_pedido_service.core.usecase;

import org.springframework.stereotype.Component;

import com.tech_challenge.fiap_pedido_service.core.dto.StatusEnum;
import com.tech_challenge.fiap_pedido_service.core.exception.PedidoNotFoundException;
import com.tech_challenge.fiap_pedido_service.core.gateway.RepositoryGateway;

@Component
public class PedidoExpiredUseCaseImpl implements PedidoExpiredUseCase {
    private final RepositoryGateway repository;

    public PedidoExpiredUseCaseImpl(RepositoryGateway repository) {
        this.repository = repository;
    }

    @Override
    public void changeToClosedExpired(String pedidoId) {
        var pedido = this.repository.findById(pedidoId).orElseThrow(PedidoNotFoundException::new);

        pedido.setStatus(StatusEnum.FECHADO_EXPIRADO);

        this.repository.save(pedido);
    }

}
