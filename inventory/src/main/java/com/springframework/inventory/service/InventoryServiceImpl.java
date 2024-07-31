package com.springframework.inventory.service;

import com.springframework.inventory.domain.Product;
import com.springframework.inventory.repository.ProductRepository;
import com.springframework.inventory.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;

    @Override
    public Flux<Product> getAllProducts() {
        return Flux.fromIterable(productRepository.findAll());
    }

    @Override
    public Mono<Product> getProductById(Integer id) {
        return Mono.just(productRepository.findById(id).orElse(null));
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return Mono.just(productRepository.save(product));
    }

    @Override
    public Mono<Product> updateProduct(Integer productId, Product productToUpdate) {
        Product existingProduct = productRepository.findById(productId).orElse(null);
        if(existingProduct == null) {
            return Mono.empty();
        }
        Product updatedProduct = updateProductData(existingProduct, productToUpdate);
        return Mono.just(productRepository.save(updatedProduct));
    }

    @Override
    public Mono<Void> deleteProduct(Integer productId) {
        Product existingProduct = productRepository.findById(productId).orElse(null);
        if(existingProduct == null) {
            return Mono.error(new RuntimeException(Constants.PRODUCT_NOT_FOUND));
        }
        productRepository.deleteById(productId);
        return Mono.fromRunnable(()->{});
    }

    Product updateProductData(Product existingProduct, Product productToUpdate) {
        existingProduct.setName(productToUpdate.getName() != null ? productToUpdate.getName() : existingProduct.getName());
        existingProduct.setQuantity(productToUpdate.getQuantity() != null ? productToUpdate.getQuantity() : existingProduct.getQuantity());
        return existingProduct;
    }

}
