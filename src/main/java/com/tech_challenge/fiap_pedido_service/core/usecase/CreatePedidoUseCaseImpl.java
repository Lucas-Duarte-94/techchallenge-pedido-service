package com.tech_challenge.fiap_pedido_service.core.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import feign.FeignException;
import jakarta.transaction.Transactional;

import com.tech_challenge.fiap_pedido_service.core.domain.entity.ItemPedido;
import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;
import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.ItemPedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.ReserveEstoqueDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.StatusEnum;
import com.tech_challenge.fiap_pedido_service.core.dto.UsuarioResponseDTO;
import com.tech_challenge.fiap_pedido_service.core.exception.OutOfStockException;
import com.tech_challenge.fiap_pedido_service.core.exception.ProductNotFoundException;
import com.tech_challenge.fiap_pedido_service.core.exception.ProductStockException;
import com.tech_challenge.fiap_pedido_service.core.exception.UsuarioServiceException;
import com.tech_challenge.fiap_pedido_service.core.gateway.EstoqueClient;
import com.tech_challenge.fiap_pedido_service.core.gateway.ProdutoClient;
import com.tech_challenge.fiap_pedido_service.core.gateway.RepositoryGateway;
import com.tech_challenge.fiap_pedido_service.core.gateway.UsuarioClient;

@Component
public class CreatePedidoUseCaseImpl implements CreatePedidoUseCase {
    private RepositoryGateway repository;
    private ProdutoClient produtoClient;
    private EstoqueClient estoqueClient;
    private UsuarioClient usuarioClient;
    private StatusEnum status;

    private final Logger logger = LoggerFactory.getLogger(CreatePedidoUseCaseImpl.class);

    public CreatePedidoUseCaseImpl(RepositoryGateway repositoryGateway,
            ProdutoClient produtoClient,
            UsuarioClient usuarioClient,
            EstoqueClient estoqueClient) {
        this.repository = repositoryGateway;
        this.produtoClient = produtoClient;
        this.estoqueClient = estoqueClient;
        this.usuarioClient = usuarioClient;
        this.status = StatusEnum.ABERTO;
    }

    @Override
    @Transactional
    public void create(CreatePedidoDTO createPedidoDTO) {
        verifyUser(createPedidoDTO.userID());

        var pedido = Pedido.builder()
                .userId(createPedidoDTO.userID())
                .paymentInfo(createPedidoDTO.paymentInfo())
                .build();

        var itensPedido = createPedidoDTO.pedidos().stream()
                .map(itemPedido -> {
                    var productExists = verifyIfProductExists(itemPedido.productSKU());

                    if (!productExists) {
                        throw new ProductNotFoundException(itemPedido.productSKU());
                    }

                    var estoqueDisponivel = verifyProductStock(itemPedido.productSKU());

                    logger.debug("productSKU: {} - estoque disponivel: {}", itemPedido.productSKU(), estoqueDisponivel);

                    if (estoqueDisponivel < itemPedido.qtd()) {
                        throw new OutOfStockException(itemPedido.productSKU());
                    }

                    return ItemPedido.builder()
                            .productSKU(itemPedido.productSKU())
                            .qtd(itemPedido.qtd())
                            .pedido(pedido)
                            .build();
                })
                .toList();

        pedido.setItens(itensPedido);
        pedido.setStatus(this.status);

        var savedPedido = this.repository.save(pedido);

        ReserveEstoqueDTO dto = new ReserveEstoqueDTO(
                savedPedido.getItens().stream()
                        .map(item -> new ItemPedidoDTO(item.getProductSKU(), item.getQtd()))
                        .toList(),
                savedPedido.getId());
        this.estoqueClient.reserveStock(dto);
    }

    private boolean verifyIfProductExists(String productSKU) {
        try {
            this.produtoClient.findByProductSKU(productSKU);

            return true;
        } catch (FeignException.NotFound ex) {
            logger.warn("Produto com SKU {} não encontrado no serviço de produtos.", productSKU);
            return false;
        } catch (Exception ex) {
            logger.error("Erro inesperado ao verificar produto " + productSKU, ex);
            return false;
        }
    }

    private int verifyProductStock(String productSKU) {
        try {
            int quantidadeDisponivel = this.estoqueClient.getEstoqueByProductSKU(productSKU).quantidadeDisponivel();

            return quantidadeDisponivel;
        } catch (FeignException.NotFound ex) {
            throw new ProductStockException(
                    "Produto com SKU " + productSKU + " não encontrado no serviço de produtos.");
        } catch (Exception ex) {
            logger.error("Erro ao consultar estoque com codigo: {}\n - Stacktrace: {}", productSKU, ex.getMessage());
            throw new ProductStockException("Erro ao consultar estoque");
        }
    }

    private UsuarioResponseDTO verifyUser(String userId) {
        try {
            var user = this.usuarioClient.getUsuariobyId(userId);

            return user;
        } catch (FeignException.NotFound ex) {
            throw new UsuarioServiceException("Usuário de id " + userId + " não encontrado.");
        } catch (Exception ex) {
            logger.error("Erro ao consultar usuário com id: {}\n - Stacktrace: {}", userId, ex.getMessage());
            throw new UsuarioServiceException("Erro ao consultar serviço de usuários.");
        }

    }

}