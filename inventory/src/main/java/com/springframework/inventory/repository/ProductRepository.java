package com.springframework.inventory.repository;

import com.springframework.inventory.domain.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity + ?2 WHERE p.id = ?1")
    void increaseQuantity(Integer productId, Integer quantityToAdd);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity - ?2 WHERE p.id = ?1")
    void decreaseQuantity(Integer productId, Integer quantityToSubtract);
}
