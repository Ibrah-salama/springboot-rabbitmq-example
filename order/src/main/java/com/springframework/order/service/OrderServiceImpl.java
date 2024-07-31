package com.springframework.order.service;

import com.springframework.amqp.OrderEvent;
import com.springframework.amqp.RabbitMqMessageProducer;
import com.springframework.order.domain.Order;
import com.springframework.order.domain.OrderItem;
import com.springframework.order.mappers.OrderMapper;
import com.springframework.order.repository.OrderRepository;
import com.springframework.order.utils.Constants;
import com.springframework.order.utils.OrderNotFoundException;
import com.springframework.order.utils.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final RabbitMqMessageProducer rabbitMQMessageProducer;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Flux<Order> listAllOrders() {
        return Flux.fromIterable(orderRepository.findAll());
    }

    @Override
    public Mono<Order> createOrder(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(order);
        }
        order.setStatus(OrderStatusEnum.CREATED);
        Order createdOrder = orderRepository.save(order);
        if (createdOrder != null) {
            OrderEvent orderEvent = orderMapper.orderToOrderEvent(createdOrder);
            rabbitMQMessageProducer.publish(orderEvent, Constants.ORDER_EXCHANGE, Constants.ORDER_INVENTORY_ROUTING_KEY);
            return Mono.just(createdOrder);
        }
        return null;
    }

    @Override
    public Mono<Order> updateOrder(Integer id, Order order) {
        Order foundOrder = orderRepository.findById(id).orElse(null);
        if (foundOrder != null && foundOrder.getStatus() != OrderStatusEnum.CANCELLED) {
            foundOrder.setStatus(order.getStatus());
            Order updatedOrder = orderRepository.save(foundOrder);
            OrderEvent orderEvent = orderMapper.orderToOrderEvent(updatedOrder);
            rabbitMQMessageProducer.publish(orderEvent, Constants.ORDER_EXCHANGE, Constants.ORDER_INVENTORY_ROUTING_KEY);
            return Mono.just(updatedOrder);
        }
        return Mono.error(new OrderNotFoundException("Order not found"));
    }
}
