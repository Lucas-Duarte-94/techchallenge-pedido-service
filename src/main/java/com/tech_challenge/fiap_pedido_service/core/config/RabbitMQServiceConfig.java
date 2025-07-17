package com.tech_challenge.fiap_pedido_service.core.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQServiceConfig {

    @Value("${app.rabbitmq.exchange:order.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.queue:order.queue}")
    private String queueName;

    @Value("${app.rabbitmq.routing-key:order.notification}")
    private String routingKey;

    // Configuração do Exchange
    @Bean
    public DirectExchange pedidoExchange() {
        return new DirectExchange(exchangeName);
    }

    // Configuração da Fila
    @Bean
    public Queue pedidoNotificationQueue() {
        return QueueBuilder
                .durable(queueName)
                .withArguments(java.util.Map.of(
                        "x-dead-letter-exchange", exchangeName + ".dlx",
                        "x-dead-letter-routing-key", routingKey + ".dlq"
                ))
                .build();
    }

    // Dead Letter Exchange (para mensagens rejeitadas)
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(exchangeName + ".dlx");
    }

    // Dead Letter Queue
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(queueName + ".dlq")
                .build();
    }

    // Binding da fila principal
    @Bean
    public Binding pedidoBinding() {
        return BindingBuilder
                .bind(pedidoNotificationQueue())
                .to(pedidoExchange())
                .with(routingKey);
    }

    // Binding da dead letter queue
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(routingKey + ".dlq");
    }

    // Conversor JSON para as mensagens
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Configuração do Listener Container
    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(1);
        return factory;
    }
}

