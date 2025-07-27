package com.tech_challenge.fiap_pedido_service.core.usecase.webhook;

import com.tech_challenge.fiap_pedido_service.core.dto.WebhookRequestDTO;

public interface WebhookUpdatePedidoUseCase {
    void execute(WebhookRequestDTO requestDTO);
}
