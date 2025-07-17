package com.tech_challenge.fiap_pedido_service.core.domain.entity;

import com.tech_challenge.fiap_pedido_service.core.dto.ItemPedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.StatusEnum;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private List<ItemPedidoDTO> itens;
    private String paymentInfo;
    private String userId;
    private StatusEnum status;
}