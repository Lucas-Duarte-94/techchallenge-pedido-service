package com.tech_challenge.fiap_pedido_service.core.controller;

import com.tech_challenge.fiap_pedido_service.core.exception.OutOfStockException;
import com.tech_challenge.fiap_pedido_service.core.exception.PedidoNotFoundException;
import com.tech_challenge.fiap_pedido_service.core.exception.ProductNotFoundException;
import com.tech_challenge.fiap_pedido_service.core.exception.ProductStockException;
import com.tech_challenge.fiap_pedido_service.core.exception.UsuarioServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionControllerTest {

    private GlobalExceptionController globalExceptionController;

    @BeforeEach
    void setUp() {
        globalExceptionController = new GlobalExceptionController();
    }

    @Test
    void handleProductNotFound() {
        ProductNotFoundException exception = new ProductNotFoundException("Product not found");
        ResponseEntity<String> response = globalExceptionController.handleProductNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Produto com código Product not found não encontrado!", response.getBody());
    }

    @Test
    void handleOutOfStockException() {
        OutOfStockException exception = new OutOfStockException("Out of stock");
        ResponseEntity<String> response = globalExceptionController.handleOutOfStockException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Estoque insuficiente para o produto: Out of stock", response.getBody());
    }

    @Test
    void handleProductStockException() {
        ProductStockException exception = new ProductStockException("Product stock error");
        ResponseEntity<String> response = globalExceptionController.handleProductStockException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product stock error", response.getBody());
    }

    @Test
    void handlePedidoNotFoundException() {
        PedidoNotFoundException exception = new PedidoNotFoundException("Pedido not found");
        ResponseEntity<String> response = globalExceptionController.handlePedidoNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Pedido not found", response.getBody());
    }

    @Test
    void handleUsuarioServiceException() {
        UsuarioServiceException exception = new UsuarioServiceException("User service error");
        ResponseEntity<String> response = globalExceptionController.handleUsuarioServiceException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User service error", response.getBody());
    }
}
