package com.tech_challenge.fiap_pedido_service.core.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;

@Repository
public interface RepositoryGateway extends JpaRepository<Pedido, String> {

    Optional<Pedido> findById(String id);

    List<Pedido> findByUserId(String userId);

}
