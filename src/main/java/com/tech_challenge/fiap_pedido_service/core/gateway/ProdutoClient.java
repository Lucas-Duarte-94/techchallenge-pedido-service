package com.tech_challenge.fiap_pedido_service.core.gateway;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Produto;

@FeignClient(name = "produto-service", url = "http://localhost:8082") // porta do produto-service
public interface ProdutoClient {
    @GetMapping("/produto")
    List<Produto> buscarProdutos();
}