package com.springframework.order.mappers;

import com.springframework.amqp.OrderEvent;
import com.springframework.order.domain.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "status", target = "status")
    @Mapping(source = "id", target = "id")
    @Mapping(expression = "java(com.springframework.order.utils.MapperUtils.mapOrderProductsToProductEventProducts(order.getOrderItems()))", target = "products")
    OrderEvent orderToOrderEvent(Order order);
}
