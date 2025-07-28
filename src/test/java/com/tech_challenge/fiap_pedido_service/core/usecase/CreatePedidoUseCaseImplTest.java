
package com.tech_challenge.fiap_pedido_service.core.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;
import com.tech_challenge.fiap_pedido_service.core.domain.entity.Produto;
import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.EstoqueResponseDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.ItemPedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.PaymentInfoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.UsuarioResponseDTO;
import com.tech_challenge.fiap_pedido_service.core.exception.ProductNotFoundException;
import com.tech_challenge.fiap_pedido_service.core.exception.ProductStockException;
import com.tech_challenge.fiap_pedido_service.core.exception.RequestException;
import com.tech_challenge.fiap_pedido_service.core.exception.UsuarioServiceException;
import com.tech_challenge.fiap_pedido_service.core.gateway.EstoqueClient;
import com.tech_challenge.fiap_pedido_service.core.gateway.PagamentoClient;
import com.tech_challenge.fiap_pedido_service.core.gateway.ProdutoClient;
import com.tech_challenge.fiap_pedido_service.core.gateway.RepositoryGateway;
import com.tech_challenge.fiap_pedido_service.core.gateway.UsuarioClient;

import feign.FeignException;

class CreatePedidoUseCaseImplTest {

    @Mock
    private RepositoryGateway repository;

    @Mock
    private ProdutoClient produtoClient;

    @Mock
    private EstoqueClient estoqueClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private PagamentoClient pagamentoClient;

    @InjectMocks
    private CreatePedidoUseCaseImpl createPedidoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        CreatePedidoDTO createPedidoDTO = new CreatePedidoDTO(Collections.singletonList(new ItemPedidoDTO("sku123", 1, BigDecimal.TEN)), "user123", new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));

        when(usuarioClient.getUsuariobyId("user123")).thenReturn(new UsuarioResponseDTO("user123", "Test User"));
        Produto produto = new Produto();
        produto.setPreco(BigDecimal.TEN);
        when(produtoClient.findByProductSKU("sku123")).thenReturn(ResponseEntity.ok(produto));
        when(estoqueClient.getEstoqueByProductSKU("sku123")).thenReturn(new EstoqueResponseDTO("sku123", 10));
        Pedido savedPedido = new Pedido();
        savedPedido.setItens(Collections.emptyList());
        savedPedido.setPaymentInfo(new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));
        when(repository.save(any(Pedido.class))).thenReturn(savedPedido);
        doNothing().when(estoqueClient).reserveStock(any());
        doNothing().when(pagamentoClient).createPayment(any());

        createPedidoUseCase.create(createPedidoDTO);

        verify(repository, times(1)).save(any(Pedido.class));
        verify(estoqueClient, times(1)).reserveStock(any());
        verify(pagamentoClient, times(1)).createPayment(any());
    }

    @Test
    void testCreateWithUserNotFound() {
        CreatePedidoDTO createPedidoDTO = new CreatePedidoDTO(Collections.singletonList(new ItemPedidoDTO("sku123", 1, BigDecimal.TEN)), "user123", new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));

        when(usuarioClient.getUsuariobyId("user123")).thenThrow(FeignException.NotFound.class);

        try {
            createPedidoUseCase.create(createPedidoDTO);
        } catch (UsuarioServiceException e) {
            // expected
        }

        verify(repository, times(0)).save(any(Pedido.class));
    }

    @Test
    void testCreateWithProductNotFound() {
        CreatePedidoDTO createPedidoDTO = new CreatePedidoDTO(Collections.singletonList(new ItemPedidoDTO("sku123", 1, BigDecimal.TEN)), "user123", new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));

        when(usuarioClient.getUsuariobyId("user123")).thenReturn(new UsuarioResponseDTO("user123", "Test User"));
        when(produtoClient.findByProductSKU("sku123")).thenThrow(FeignException.NotFound.class);

        try {
            createPedidoUseCase.create(createPedidoDTO);
        } catch (ProductNotFoundException e) {
            // expected
        }

        verify(repository, times(0)).save(any(Pedido.class));
    }

    @Test
    void testCreateWithStockNotFound() {
        CreatePedidoDTO createPedidoDTO = new CreatePedidoDTO(Collections.singletonList(new ItemPedidoDTO("sku123", 1, BigDecimal.TEN)), "user123", new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));

        when(usuarioClient.getUsuariobyId("user123")).thenReturn(new UsuarioResponseDTO("user123", "Test User"));
        Produto produto = new Produto();
        produto.setPreco(BigDecimal.TEN);
        when(produtoClient.findByProductSKU("sku123")).thenReturn(ResponseEntity.ok(produto));
        when(estoqueClient.getEstoqueByProductSKU("sku123")).thenThrow(FeignException.NotFound.class);

        try {
            createPedidoUseCase.create(createPedidoDTO);
        } catch (ProductStockException e) {
            // expected
        }

        verify(repository, times(0)).save(any(Pedido.class));
    }

    @Test
    void testCreateWithPaymentError() {
        CreatePedidoDTO createPedidoDTO = new CreatePedidoDTO(Collections.singletonList(new ItemPedidoDTO("sku123", 1, BigDecimal.TEN)), "user123", new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));

        when(usuarioClient.getUsuariobyId("user123")).thenReturn(new UsuarioResponseDTO("user123", "Test User"));
        Produto produto = new Produto();
        produto.setPreco(BigDecimal.TEN);
        when(produtoClient.findByProductSKU("sku123")).thenReturn(ResponseEntity.ok(produto));
        when(estoqueClient.getEstoqueByProductSKU("sku123")).thenReturn(new EstoqueResponseDTO("sku123", 10));
        Pedido savedPedido = new Pedido();
        savedPedido.setItens(Collections.emptyList());
        savedPedido.setPaymentInfo(new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));
        when(repository.save(any(Pedido.class))).thenReturn(savedPedido);
        doNothing().when(estoqueClient).reserveStock(any());
        doThrow(FeignException.class).when(pagamentoClient).createPayment(any());

        try {
            createPedidoUseCase.create(createPedidoDTO);
        } catch (RequestException e) {
            // expected
        }

        verify(repository, times(1)).save(any(Pedido.class));
        verify(estoqueClient, times(1)).reserveStock(any());
    }
}
