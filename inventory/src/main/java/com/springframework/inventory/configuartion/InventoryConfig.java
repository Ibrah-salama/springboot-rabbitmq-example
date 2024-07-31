package com.springframework.inventory.configuartion;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryConfig {

    @Value("${rabbitmq.queues.orders}")
    private String ordersQueue;
    @Value("${rabbitmq.exchanges.orders}")
    private String ordersExchange;
    @Value("${rabbitmq.routing-keys.orders-inventory}")
    private String orderInventoryRoutingKey;

    @Bean
    public Queue ordersQueue() {
        return new Queue(ordersQueue);
    }

    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(ordersExchange);
    }

    @Bean
    public Binding orderInventoryBinding() {
        return BindingBuilder
                .bind(ordersQueue())
                .to(ordersExchange())
                .with(this.orderInventoryRoutingKey);
    }
}
