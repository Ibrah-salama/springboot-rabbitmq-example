package com.springframework.amqp;

import lombok.Data;

@Data
public class ProductEvent {
    private Integer id;
    private String name;
    private Integer quantity;
    private String price;
}
