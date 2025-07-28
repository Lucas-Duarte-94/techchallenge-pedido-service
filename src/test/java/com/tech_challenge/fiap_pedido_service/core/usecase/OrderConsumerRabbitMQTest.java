package com.tech_challenge.fiap_pedido_service.core.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.rabbitmq.client.Channel;
import com.tech_challenge.fiap_pedido_service.core.dto.CreatePedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.ItemPedidoDTO;
import com.tech_challenge.fiap_pedido_service.core.dto.PaymentInfoDTO;

class OrderConsumerRabbitMQTest {

    @Mock
    private CreatePedidoUseCase createPedidoUseCase;

    @Mock
    private Channel channel;

    @InjectMocks
    private OrderConsumerRabbitMQ orderConsumerRabbitMQ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListenSuccess() throws IOException {
        CreatePedidoDTO pedidoDTO = new CreatePedidoDTO(Collections.singletonList(new ItemPedidoDTO("sku123", 1, BigDecimal.TEN)), "user123", new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryTag(1L);
        Message message = new Message(new byte[0], messageProperties);

        doNothing().when(createPedidoUseCase).create(any(CreatePedidoDTO.class));

        orderConsumerRabbitMQ.listen(pedidoDTO, message, channel);

        verify(createPedidoUseCase, times(1)).create(pedidoDTO);
        verify(channel, times(1)).basicAck(eq(1L), eq(false));
        verify(channel, times(0)).basicNack(any(Long.class), any(Boolean.class), any(Boolean.class));
    }

    @Test
    void testListenFailure() throws IOException {
        CreatePedidoDTO pedidoDTO = new CreatePedidoDTO(Collections.singletonList(new ItemPedidoDTO("sku123", 1, BigDecimal.TEN)), "user123", new PaymentInfoDTO("pedido123", BigDecimal.TEN, null, null));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryTag(1L);
        Message message = new Message(new byte[0], messageProperties);

        doThrow(new RuntimeException("Test Exception")).when(createPedidoUseCase).create(any(CreatePedidoDTO.class));

        try {
            orderConsumerRabbitMQ.listen(pedidoDTO, message, channel);
        } catch (RuntimeException e) {
            // Expected exception
        }

        verify(createPedidoUseCase, times(1)).create(pedidoDTO);
        verify(channel, times(0)).basicAck(any(Long.class), any(Boolean.class));
        verify(channel, times(1)).basicNack(eq(1L), eq(false), eq(false));
    }
}