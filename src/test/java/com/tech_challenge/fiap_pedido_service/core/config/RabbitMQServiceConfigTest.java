package com.tech_challenge.fiap_pedido_service.core.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestPropertySource(properties = {
        "app.rabbitmq.exchange=test.exchange",
        "app.rabbitmq.queue=test.queue",
        "app.rabbitmq.routing-key=test.routing"
})
class RabbitMQServiceConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void pedidoExchangeBeanExistsAndHasCorrectName() {
        DirectExchange exchange = applicationContext.getBean("pedidoExchange", DirectExchange.class);
        assertNotNull(exchange);
        assertEquals("test.exchange", exchange.getName());
    }

    @Test
    void pedidoNotificationQueueBeanExistsAndHasCorrectName() {
        Queue queue = applicationContext.getBean("pedidoNotificationQueue", Queue.class);
        assertNotNull(queue);
        assertEquals("test.queue", queue.getName());
    }

    @Test
    void deadLetterExchangeBeanExistsAndHasCorrectName() {
        DirectExchange deadLetterExchange = applicationContext.getBean("deadLetterExchange", DirectExchange.class);
        assertNotNull(deadLetterExchange);
        assertEquals("test.exchange.dlx", deadLetterExchange.getName());
    }

    @Test
    void deadLetterQueueBeanExistsAndHasCorrectName() {
        Queue deadLetterQueue = applicationContext.getBean("deadLetterQueue", Queue.class);
        assertNotNull(deadLetterQueue);
        assertEquals("test.queue.dlq", deadLetterQueue.getName());
    }

    @Test
    void pedidoBindingBeanExistsAndHasCorrectProperties() {
        Binding binding = applicationContext.getBean("pedidoBinding", Binding.class);
        assertNotNull(binding);
        assertEquals("test.queue", binding.getDestination());
        assertEquals("test.exchange", binding.getExchange());
        assertEquals("test.routing", binding.getRoutingKey());
    }

    @Test
    void deadLetterBindingBeanExistsAndHasCorrectProperties() {
        Binding deadLetterBinding = applicationContext.getBean("deadLetterBinding", Binding.class);
        assertNotNull(deadLetterBinding);
        assertEquals("test.queue.dlq", deadLetterBinding.getDestination());
        assertEquals("test.exchange.dlx", deadLetterBinding.getExchange());
        assertEquals("test.routing.dlq", deadLetterBinding.getRoutingKey());
    }

    @Test
    void messageConverterBeanExists() {
        Jackson2JsonMessageConverter converter = applicationContext.getBean(Jackson2JsonMessageConverter.class);
        assertNotNull(converter);
    }

    @Test
    void rabbitListenerContainerFactoryBeanExists() {
        // Mock ConnectionFactory as it's an external dependency
        ConnectionFactory mockConnectionFactory = mock(ConnectionFactory.class);
        // Get the config class instance from the Spring context
        RabbitMQServiceConfig config = applicationContext.getBean(RabbitMQServiceConfig.class);

        SimpleRabbitListenerContainerFactory factory = (SimpleRabbitListenerContainerFactory) config.rabbitListenerContainerFactory(mockConnectionFactory);
        assertNotNull(factory);
    }
}