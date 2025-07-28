package com.tech_challenge.fiap_pedido_service.core.usecase;

import static org.mockito.ArgumentMatchers.any;
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
import com.tech_challenge.fiap_pedido_service.core.dto.StatusEnum;
import com.tech_challenge.fiap_pedido_service.core.exception.PedidoNotFoundException;
import com.tech_challenge.fiap_pedido_service.core.gateway.RepositoryGateway;

class PedidoExpiredUseCaseImplTest {

    @Mock
    private RepositoryGateway repository;

    @InjectMocks
    private PedidoExpiredUseCaseImpl pedidoExpiredUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangeToClosedExpired() {
        String pedidoId = "pedido123";
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(StatusEnum.ABERTO);

        when(repository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(repository.save(any(Pedido.class))).thenReturn(pedido);

        pedidoExpiredUseCase.changeToClosedExpired(pedidoId);

        verify(repository, times(1)).findById(pedidoId);
        verify(repository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testChangeToClosedExpiredPedidoNotFound() {
        String pedidoId = "pedido123";

        when(repository.findById(pedidoId)).thenReturn(Optional.empty());

        try {
            pedidoExpiredUseCase.changeToClosedExpired(pedidoId);
        } catch (PedidoNotFoundException e) {
            // Expected exception
        }

        verify(repository, times(1)).findById(pedidoId);
        verify(repository, times(0)).save(any(Pedido.class));
    }
}