package com.tech_challenge.fiap_pedido_service.core.usecase.webhook;

import org.springframework.stereotype.Service;

import com.tech_challenge.fiap_pedido_service.core.dto.WebhookRequestDTO;

@Service
public class WebhookUseCaseImpl implements WebhookUseCase {
    private final WebhookUpdatePedidoUseCase updatePedidoUseCase;

    public WebhookUseCaseImpl(WebhookUpdatePedidoUseCase updatePedidoUseCase) {
        this.updatePedidoUseCase = updatePedidoUseCase;
    }

    @Override
    public void updatePedido(WebhookRequestDTO requestDTO) {
        this.updatePedidoUseCase.execute(requestDTO);
    }

}
