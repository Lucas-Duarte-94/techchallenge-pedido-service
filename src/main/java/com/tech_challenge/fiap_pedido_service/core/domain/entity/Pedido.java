package com.tech_challenge.fiap_pedido_service.core.domain.entity;

import com.tech_challenge.fiap_pedido_service.core.dto.PaymentInfoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.StatusEnum;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;
    @Transient
    private PaymentInfoDTO paymentInfo;
    private BigDecimal total;
    private String userId;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}