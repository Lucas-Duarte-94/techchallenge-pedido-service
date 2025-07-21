package com.tech_challenge.fiap_pedido_service.core.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Produto;
import com.tech_challenge.fiap_pedido_service.core.gateway.ProdutoClient;

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
}