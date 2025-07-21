package com.tech_challenge.fiap_pedido_service.core.usecase;

import com.rabbitmq.client.Channel;
import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;
import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.StatusEnum;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;

import java.io.IOException;

@Component
public class OrderConsumerRabbitMQ {

    private final Logger logger = LoggerFactory.getLogger(OrderConsumerRabbitMQ.class);
    private CreatePedidoUseCase createPedidoUseCase;

    public OrderConsumerRabbitMQ(CreatePedidoUseCase createPedidoUseCase) {
        this.createPedidoUseCase = createPedidoUseCase;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void listen(CreatePedidoDTO pedidoDTO, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            this.createPedidoUseCase.create(pedidoDTO);

            channel.basicAck(deliveryTag, false);
            logger.info("✅ NOTIFICAÇÃO PROCESSADA COM SUCESSO!");
        } catch (Exception e) {
            logger.error("❌ ERRO ao processar notificação: {}", e.getMessage(), e);

            // Rejeita a mensagem e envia para Dead Letter Queue
            channel.basicNack(deliveryTag, false, false);
            logger.error("🗑️ Mensagem enviada para Dead Letter Queue");
        }
    }

}
