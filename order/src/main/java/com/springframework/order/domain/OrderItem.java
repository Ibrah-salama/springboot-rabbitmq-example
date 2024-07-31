package com.springframework.order.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @SequenceGenerator(
            name = "order_item_id_sequence",
            sequenceName = "order_item_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_item_id_sequence"
    )
    private Integer id;
    private String name;
    private Integer quantity;
    private String price;
    private Integer productId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price='" + price + '\'' +
                ", productId=" + productId +
                ", order=" + order.id +
                '}';
    }
}
