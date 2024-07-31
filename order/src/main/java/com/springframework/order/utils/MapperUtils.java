package com.springframework.order.utils;

import com.springframework.amqp.ProductEvent;
import com.springframework.order.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {

    public static List<ProductEvent> mapOrderProductsToProductEventProducts(List<OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> {
            ProductEvent productEvent = new ProductEvent();
            productEvent.setId(orderItem.getProductId());
            productEvent.setName(orderItem.getName());
            productEvent.setQuantity(orderItem.getQuantity());
            productEvent.setPrice(orderItem.getPrice());
            return productEvent;
        }).collect(Collectors.toList());
    }

}
