package com.tech_challenge.fiap_pedido_service.core.controller;

import com.tech_challenge.fiap_pedido_service.core.dto.PaymentStatus;
import com.tech_challenge.fiap_pedido_service.core.dto.WebhookRequestDTO;
import com.tech_challenge.fiap_pedido_service.core.usecase.webhook.WebhookUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class WebhookControllerTest {

    @Mock
    private WebhookUseCase webhookUseCase;

    @InjectMocks
    private WebhookController webhookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleWebhook() {
        WebhookRequestDTO requestDTO = new WebhookRequestDTO("1", PaymentStatus.APROVADO);

        ResponseEntity<Void> response = webhookController.handleWebhook(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(webhookUseCase).updatePedido(requestDTO);
    }
}
