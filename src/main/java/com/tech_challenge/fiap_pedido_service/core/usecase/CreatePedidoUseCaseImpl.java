package com.tech_challenge.fiap_pedido_service.core.usecase;

import org.springframework.stereotype.Component;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.ItemPedido;
import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;
import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.StatusEnum;
import com.tech_challenge.fiap_pedido_service.core.gateway.RepositoryGateway;

@Component
public class CreatePedidoUseCaseImpl implements CreatePedidoUseCase {
    private RepositoryGateway repository;

    public CreatePedidoUseCaseImpl(RepositoryGateway repositoryGateway) {
        this.repository = repositoryGateway;
    }

    @Override
    public void create(CreatePedidoDTO createPedidoDTO) {
        var itensPedido = createPedidoDTO.pedidos().stream()
                .map(itemPedido -> ItemPedido.builder()
                        .productSKU(itemPedido.productSKU())
                        .qtd(itemPedido.qtd())
                        .build())
                .toList();

        var pedido = Pedido.builder()
                .userId(createPedidoDTO.userID())
                .paymentInfo(createPedidoDTO.paymentInfo())
                .status(StatusEnum.ABERTO)
                .itens(itensPedido)
                .build();

        this.repository.save(pedido);
    }

}
