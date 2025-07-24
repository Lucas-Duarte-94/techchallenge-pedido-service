package com.tech_challenge.fiap_pedido_service.core.gateway;

import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Produto;

@FeignClient(name = "produto-service", url = "${produto.service.url:http://localhost:8082/produto}")
public interface ProdutoClient {
    @GetMapping()
    List<Produto> buscarProdutos();

    @GetMapping("/{product_sku}")
    ResponseEntity<Produto> findByProductSKU(@PathVariable("product_sku") String product_sku);
}