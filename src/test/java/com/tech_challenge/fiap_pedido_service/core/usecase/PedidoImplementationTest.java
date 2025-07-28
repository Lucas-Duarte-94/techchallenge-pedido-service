package com.tech_challenge.fiap_pedido_service.core.usecase;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.EstoqueRequestDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.ItemPedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.PaymentInfoDTO;

class PedidoImplementationTest {

    @Mock
    private CreatePedidoUseCase createPedidoUseCase;

    @Mock
    private PedidoExpiredUseCase pedidoExpiredUseCase;

    @Mock
    private PedidoOutOfStockUseCase pedidoOutOfStockUseCase;

    @Mock
    private PedidoPaymentFailUseCase pedidoPaymentFailUseCase;

    @Mock
    private PedidoSuccessUseCase pedidoSuccessUseCase;

    @InjectMocks
    private PedidoImplementation pedidoImplementation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePedido() {
        CreatePedidoDTO createPedidoDTO = new CreatePedidoDTO(Collections.singletonList(new ItemPedidoDTO("sku123", 1, BigDecimal.TEN)), "user123", new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));
        pedidoImplementation.createPedido(createPedidoDTO);
        verify(createPedidoUseCase, times(1)).create(createPedidoDTO);
    }

    @Test
    void testChangeToClosedOutOfStock() {
        EstoqueRequestDTO request = new EstoqueRequestDTO("pedido123");
        pedidoImplementation.changeToClosedOutOfStock(request);
        verify(pedidoOutOfStockUseCase, times(1)).changeToClosedOutOfStock("pedido123");
    }

    @Test
    void testChangeToClosedPaymentFail() {
        EstoqueRequestDTO request = new EstoqueRequestDTO("pedido123");
        pedidoImplementation.changeToClosedPaymentFail(request);
        verify(pedidoPaymentFailUseCase, times(1)).changeToClosedPaymentFail("pedido123");
    }

    @Test
    void testChangeToClosedSuccess() {
        EstoqueRequestDTO request = new EstoqueRequestDTO("pedido123");
        pedidoImplementation.changeToClosedSuccess(request);
        verify(pedidoSuccessUseCase, times(1)).changeToClosedSuccess("pedido123");
    }

    @Test
    void testChangeToClosedExpired() {
        EstoqueRequestDTO request = new EstoqueRequestDTO("pedido123");
        pedidoImplementation.changeToClosedExpired(request);
        verify(pedidoExpiredUseCase, times(1)).changeToClosedExpired("pedido123");
    }
}