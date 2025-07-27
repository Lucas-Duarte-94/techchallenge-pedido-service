package com.tech_challenge.fiap_pedido_service.core.usecase.webhook;

import org.springframework.stereotype.Component;

import com.tech_challenge.fiap_pedido_service.core.dto.PaymentStatus;
import com.tech_challenge.fiap_pedido_service.core.dto.PedidoIdRequestDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.StatusEnum;
import com.tech_challenge.fiap_pedido_service.core.dto.UpdatePagamentoRequestDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.WebhookRequestDTO;
import com.tech_challenge.fiap_pedido_service.core.exception.PedidoNotFoundException;
import com.tech_challenge.fiap_pedido_service.core.exception.RequestException;
import com.tech_challenge.fiap_pedido_service.core.gateway.EstoqueClient;
import com.tech_challenge.fiap_pedido_service.core.gateway.PagamentoClient;
import com.tech_challenge.fiap_pedido_service.core.gateway.RepositoryGateway;

import feign.FeignException.FeignClientException;
import jakarta.transaction.Transactional;

@Component
public class WebhookUpdatePedidoUseCaseImpl implements WebhookUpdatePedidoUseCase {
    private final RepositoryGateway pedidoRepository;
    private final PagamentoClient pagamentoClient;
    private final EstoqueClient estoqueClient;

    public WebhookUpdatePedidoUseCaseImpl(RepositoryGateway pedidoRepository, PagamentoClient pagamentoClient,
            EstoqueClient estoqueClient) {
        this.pedidoRepository = pedidoRepository;
        this.pagamentoClient = pagamentoClient;
        this.estoqueClient = estoqueClient;
    }

    @Override
    @Transactional
    public void execute(WebhookRequestDTO requestDTO) {
        var pedido = this.pedidoRepository.findById(requestDTO.pedidoId()).orElseThrow(PedidoNotFoundException::new);

        if (requestDTO.status() == PaymentStatus.RECUSADO) {
            pedido.setStatus(StatusEnum.FECHADO_SEM_CREDITO);
        }

        if (requestDTO.status() == PaymentStatus.APROVADO) {
            pedido.setStatus(StatusEnum.FECHADO_COM_SUCESSO);
        }

        this.pedidoRepository.save(pedido);

        try {
            this.pagamentoClient.updatePagamento(new UpdatePagamentoRequestDTO(pedido.getId(), requestDTO.status()));
            this.estoqueClient.changeToConfirmedStatus(new PedidoIdRequestDTO(pedido.getId()));

        } catch (FeignClientException ex) {
            throw new RequestException("Erro ao fazer uma requisição. - Stacktrace: " + ex.getMessage());
        }
    }

}
