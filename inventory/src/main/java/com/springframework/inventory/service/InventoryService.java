package com.springframework.inventory.service;

import com.springframework.inventory.domain.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InventoryService {
    Flux<Product> getAllProducts();
    Mono<Product> getProductById(Integer id);
    Mono<Product> saveProduct(Product product);
    Mono<Product> updateProduct(Integer productId, Product productToUpdate);
    Mono<Void> deleteProduct(Integer id);
}
