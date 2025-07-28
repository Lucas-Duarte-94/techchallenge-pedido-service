package com.tech_challenge.fiap_pedido_service.core.usecase.webhook;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;
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

import feign.FeignException;

class WebhookUpdatePedidoUseCaseImplTest {

    @Mock
    private RepositoryGateway pedidoRepository;

    @Mock
    private PagamentoClient pagamentoClient;

    @Mock
    private EstoqueClient estoqueClient;

    @InjectMocks
    private WebhookUpdatePedidoUseCaseImpl webhookUpdatePedidoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteApprovedStatus() {
        String pedidoId = "pedido123";
        WebhookRequestDTO requestDTO = new WebhookRequestDTO(pedidoId, PaymentStatus.APROVADO);
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(StatusEnum.ABERTO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        doNothing().when(pagamentoClient).updatePagamento(any(UpdatePagamentoRequestDTO.class));
        doNothing().when(estoqueClient).changeToConfirmedStatus(any(PedidoIdRequestDTO.class));

        webhookUpdatePedidoUseCase.execute(requestDTO);

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(1)).save(pedido);
        verify(pagamentoClient, times(1)).updatePagamento(any(UpdatePagamentoRequestDTO.class));
        verify(estoqueClient, times(1)).changeToConfirmedStatus(any(PedidoIdRequestDTO.class));
    }

    @Test
    void testExecuteRejectedStatus() {
        String pedidoId = "pedido123";
        WebhookRequestDTO requestDTO = new WebhookRequestDTO(pedidoId, PaymentStatus.RECUSADO);
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(StatusEnum.ABERTO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        doNothing().when(pagamentoClient).updatePagamento(any(UpdatePagamentoRequestDTO.class));
        doNothing().when(estoqueClient).changeToConfirmedStatus(any(PedidoIdRequestDTO.class));

        webhookUpdatePedidoUseCase.execute(requestDTO);

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(1)).save(pedido);
        verify(pagamentoClient, times(1)).updatePagamento(any(UpdatePagamentoRequestDTO.class));
        verify(estoqueClient, times(1)).changeToConfirmedStatus(any(PedidoIdRequestDTO.class));
    }

    @Test
    void testExecutePedidoNotFound() {
        String pedidoId = "pedido123";
        WebhookRequestDTO requestDTO = new WebhookRequestDTO(pedidoId, PaymentStatus.APROVADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        try {
            webhookUpdatePedidoUseCase.execute(requestDTO);
        } catch (PedidoNotFoundException e) {
            // Expected exception
        }

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(0)).save(any(Pedido.class));
        verify(pagamentoClient, times(0)).updatePagamento(any(UpdatePagamentoRequestDTO.class));
        verify(estoqueClient, times(0)).changeToConfirmedStatus(any(PedidoIdRequestDTO.class));
    }

    @Test
    void testExecutePaymentClientError() {
        String pedidoId = "pedido123";
        WebhookRequestDTO requestDTO = new WebhookRequestDTO(pedidoId, PaymentStatus.APROVADO);
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(StatusEnum.ABERTO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        doThrow(FeignException.class).when(pagamentoClient).updatePagamento(any(UpdatePagamentoRequestDTO.class));

        try {
            webhookUpdatePedidoUseCase.execute(requestDTO);
        } catch (RequestException e) {
            // Expected exception
        }

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(1)).save(pedido);
        verify(pagamentoClient, times(1)).updatePagamento(any(UpdatePagamentoRequestDTO.class));
        verify(estoqueClient, times(0)).changeToConfirmedStatus(any(PedidoIdRequestDTO.class));
    }
}