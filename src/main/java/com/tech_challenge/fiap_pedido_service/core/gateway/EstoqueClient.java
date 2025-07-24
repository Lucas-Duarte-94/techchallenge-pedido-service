package com.tech_challenge.fiap_pedido_service.core.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tech_challenge.fiap_pedido_service.core.dto.EstoqueResponseDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.ReserveEstoqueDTO;

@FeignClient(name = "estoque-service", url = "${estoque.service.url:http://localhost:8083/estoque}")
public interface EstoqueClient {
    @GetMapping("/{product_sku}")
    EstoqueResponseDTO getEstoqueByProductSKU(@PathVariable("product_sku") String productSKU);

    @PostMapping
    void reserveStock(ReserveEstoqueDTO pedidoDTO);

    @PostMapping("/cancel")
    void changeToCancelStatus(String pedidoId);

    @PostMapping("/confirm")
    void changeToConfirmedStatus(String pedidoId);
}
