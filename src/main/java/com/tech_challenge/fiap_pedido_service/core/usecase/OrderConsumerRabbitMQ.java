package com.tech_challenge.fiap_pedido_service.core.usecase;

import com.rabbitmq.client.Channel;
import com.tech_challenge.fiap_pedido_service.core.domain.entity.Pedido;
import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;

import java.io.IOException;

@Component
public class OrderConsumerRabbitMQ {

    private final Logger logger = LoggerFactory.getLogger(OrderConsumerRabbitMQ.class);

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void listen(CreatePedidoDTO pedidoDTO, Message message, Channel channel) throws IOException {

            var pedido = Pedido.builder()
                    .paymentInfo(pedidoDTO.paymentInfo())
                    .userId(pedidoDTO.userID())
                    .status(StatusEnum.ABERTO)
                    .itens(pedidoDTO.pedidos())
                    .build();

            long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            channel.basicAck(deliveryTag, false);
            logger.info("✅ NOTIFICAÇÃO PROCESSADA COM SUCESSO!");
        }catch (Exception e){
            logger.error("❌ ERRO ao processar notificação: {}", e.getMessage(), e);

            // Rejeita a mensagem e envia para Dead Letter Queue
            channel.basicNack(deliveryTag, false, false);
            logger.error("🗑️ Mensagem enviada para Dead Letter Queue");
        }
    }

}
