package com.springframework.inventory.rabbitmq;

import com.springframework.amqp.OrderEvent;
import com.springframework.inventory.repository.ProductRepository;
import com.springframework.inventory.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.file.LinkOption;

@Slf4j
@Component
@AllArgsConstructor
public class InventoryConsumer {

    private final ProductRepository productRepository;

    @RabbitListener(queues = "${rabbitmq.queues.orders}")
    public void consumer(OrderEvent orderEvent) {
        log.info("orderEvent.getStatus(): {}", orderEvent.getStatus());
        if(Constants.ORDER_CREATED.equals(orderEvent.getStatus())) {
            log.info("Order {} is created. Adding products to inventory", orderEvent.getId());
            orderEvent.getProducts().forEach(product -> {
                log.info("D product.getId(): {}", product.getId());
                productRepository.decreaseQuantity(product.getId(), product.getQuantity());
            });
        } else if(Constants.ORDER_CANCELLED.equals(orderEvent.getStatus())) {
            log.info("Order {} is cancelled. Restoring products to inventory", orderEvent.getId());
            orderEvent.getProducts().forEach(product -> {
                log.info("I product.getId(): {}", product.getId());
                productRepository.increaseQuantity(product.getId(), product.getQuantity());
            });
        }

    }
}
