package com.tech_challenge.fiap_pedido_service.core.controller;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Produto;
import com.tech_challenge.fiap_pedido_service.core.dto.EstoqueRequestDTO;
import com.tech_challenge.fiap_pedido_service.core.gateway.ProdutoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PedidoControllerTest {

    @Mock
    private ProdutoClient produtoClient;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarProdutos() {
        Produto produto1 = new Produto();
        produto1.setProductSKU("SKU001");
        produto1.setDescription("Produto 1");
        produto1.setPreco(BigDecimal.valueOf(10.0));
        Produto produto2 = new Produto();
        produto2.setProductSKU("SKU002");
        produto2.setDescription("Produto 2");
        produto2.setPreco(BigDecimal.valueOf(20.0));
        List<Produto> produtos = Arrays.asList(produto1, produto2);

        when(produtoClient.buscarProdutos()).thenReturn(produtos);

        ResponseEntity<List<Produto>> response = pedidoController.listarProdutos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Produto 1", response.getBody().get(0).getDescription());
    }

    @Test
    void changeToClosedOutOfStock() {
        EstoqueRequestDTO request = new EstoqueRequestDTO("pedido123");
        ResponseEntity<Void> response = pedidoController.changeToClosedOutOfStock(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void changeToClosedPaymentFail() {
        EstoqueRequestDTO request = new EstoqueRequestDTO("pedido123");
        ResponseEntity<Void> response = pedidoController.changeToClosedPaymentFail(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void changeToClosedSuccess() {
        EstoqueRequestDTO request = new EstoqueRequestDTO("pedido123");
        ResponseEntity<Void> response = pedidoController.changeToClosedSuccess(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void changeToClosedExpired() {
        EstoqueRequestDTO request = new EstoqueRequestDTO("pedido123");
        ResponseEntity<Void> response = pedidoController.changeToClosedExpired(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
