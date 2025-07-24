package com.tech_challenge.fiap_pedido_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.tech_challenge.fiap_pedido_service.core.config.FeignConfig;

@SpringBootApplication
@EnableFeignClients
@Import(FeignConfig.class)
public class FiapPedidoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiapPedidoServiceApplication.class, args);
	}

}
