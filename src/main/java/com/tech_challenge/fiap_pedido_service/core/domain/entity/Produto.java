package com.tech_challenge.fiap_pedido_service.core.domain.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
    private String productSKU;
    private String description;
    private BigDecimal preco;
}
