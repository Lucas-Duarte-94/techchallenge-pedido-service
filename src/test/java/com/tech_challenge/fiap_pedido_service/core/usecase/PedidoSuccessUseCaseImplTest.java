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
import com.tech_challenge.fiap_pedido_service.core.exception.PedidoNotFoundException;
import com.tech_challenge.fiap_pedido_service.core.gateway.RepositoryGateway;

class PedidoSuccessUseCaseImplTest {

    @Mock
    private RepositoryGateway repository;

    @InjectMocks
    private PedidoSuccessUseCaseImpl pedidoSuccessUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangeToClosedSuccess() {
        String pedidoId = "pedido123";
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);

        when(repository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(repository.save(any(Pedido.class))).thenReturn(pedido);

        pedidoSuccessUseCase.changeToClosedSuccess(pedidoId);

        verify(repository, times(1)).findById(pedidoId);
        verify(repository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testChangeToClosedSuccessPedidoNotFound() {
        String pedidoId = "pedido123";

        when(repository.findById(pedidoId)).thenReturn(Optional.empty());

        try {
            pedidoSuccessUseCase.changeToClosedSuccess(pedidoId);
        } catch (PedidoNotFoundException e) {
            // Expected exception
        }

        verify(repository, times(1)).findById(pedidoId);
        verify(repository, times(0)).save(any(Pedido.class));
    }
}