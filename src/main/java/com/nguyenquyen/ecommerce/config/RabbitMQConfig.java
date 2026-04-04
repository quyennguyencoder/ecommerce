package com.nguyenquyen.ecommerce.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_EMAIL = "order_email_queue";
    public static final String EXCHANGE_ORDER = "order_exchange";
    public static final String ROUTING_KEY_EMAIL = "order_email_routingKey";

    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_EMAIL, true); // true = durable (không bị mất khi restart RabbitMQ)
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(EXCHANGE_ORDER);
    }

    @Bean
    public Binding binding(Queue emailQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(emailQueue).to(orderExchange).with(ROUTING_KEY_EMAIL);
    }

    // Converter giúp convert Object Data sang JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}