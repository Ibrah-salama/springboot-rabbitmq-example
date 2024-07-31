package com.springframework.amqp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderEvent implements Serializable {
    Integer id;
    String status;
    List<ProductEvent> products;
}
