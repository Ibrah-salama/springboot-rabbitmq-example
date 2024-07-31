package com.springframework.order.service;

import com.springframework.order.domain.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {

    Flux<Order> listAllOrders();
    Mono<Order> createOrder(Order order);

    Mono<Order> updateOrder(Integer id, Order order);

}
