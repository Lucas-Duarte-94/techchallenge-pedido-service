package com.tech_challenge.fiap_pedido_service.core.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Produto;
import com.tech_challenge.fiap_pedido_service.core.dto.EstoqueRequestDTO;
import com.tech_challenge.fiap_pedido_service.core.gateway.ProdutoClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final ProdutoClient produtoClient;

    public PedidoController(ProdutoClient produtoClient) {
        this.produtoClient = produtoClient;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        var produtos = produtoClient.buscarProdutos();
        return ResponseEntity.ok().body(produtos);
    }

    @PutMapping("/out-of-stock")
    public ResponseEntity<Void> changeToClosedOutOfStock(@RequestBody EstoqueRequestDTO request) {

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/payment-fail")
    public ResponseEntity<Void> changeToClosedPaymentFail(@RequestBody EstoqueRequestDTO request) {

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/success")
    public ResponseEntity<Void> changeToClosedSuccess(@RequestBody EstoqueRequestDTO request) {

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/expried")
    public ResponseEntity<Void> changeToClosedExpired(@RequestBody EstoqueRequestDTO request) {

        return ResponseEntity.noContent().build();
    }

}