package com.tech_challenge.fiap_pedido_service.core.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.tech_challenge.fiap_pedido_service.core.dto.PaymentInfoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.UpdatePagamentoRequestDTO;

@FeignClient(name = "pagamento-service", url = "${pagamento.service.url:http://localhost:8085/pagamento}")
public interface PagamentoClient {
    @PutMapping
    void updatePagamento(UpdatePagamentoRequestDTO requestDTO);

    @PostMapping
    void createPayment(PaymentInfoDTO requestDTO);
}
