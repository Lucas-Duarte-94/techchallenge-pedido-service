package com.tech_challenge.fiap_pedido_service.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech_challenge.fiap_pedido_service.core.dto.WebhookRequestDTO;
import com.tech_challenge.fiap_pedido_service.core.usecase.webhook.WebhookUseCase;

@RestController
@RequestMapping(("/webhook"))
public class WebhookController {
    private final WebhookUseCase webhookUseCase;

    private final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    public WebhookController(WebhookUseCase webhookUseCase) {
        this.webhookUseCase = webhookUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> handleWebhook(@RequestBody WebhookRequestDTO requestDTO) {
        logger.info("Recebendo chamada no endpoint de webhook.");
        this.webhookUseCase.updatePedido(requestDTO);

        return ResponseEntity.ok().build();
    }
}
