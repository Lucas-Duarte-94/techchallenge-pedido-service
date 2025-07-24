package com.tech_challenge.fiap_pedido_service.core.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tech_challenge.fiap_pedido_service.core.dto.UsuarioResponseDTO;

@FeignClient(name = "cliente-service", url = "${cliente.service.url:http://localhost:8084/usuario}")
public interface UsuarioClient {
    @GetMapping("/{id}")
    UsuarioResponseDTO getUsuariobyId(@PathVariable("id") String id);
}
