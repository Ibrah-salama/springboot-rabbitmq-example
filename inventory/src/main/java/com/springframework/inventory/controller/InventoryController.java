package com.springframework.inventory.controller;

import com.springframework.inventory.domain.Product;
import com.springframework.inventory.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public Flux<Product> getAllProducts() {
        try {
            Flux<Product> products = inventoryService.getAllProducts();
            return products;
        } catch (Exception e) {
            log.error("Error occurred while fetching products", e);
            throw new RuntimeException("Error occurred while fetching products");
        }
    }

    @GetMapping
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable Integer id) {
        return inventoryService.getProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> createProduct(@RequestBody Product product) {
        try {
            Mono<Product> savedProduct = inventoryService.saveProduct(product);
            return savedProduct;
        } catch (Exception e) {
            log.error("Error occurred while saving product", e);
//            throw new RuntimeException("Error occurred while saving product");
            return Mono.error(new RuntimeException("Error occurred while saving product"));
        }
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable Integer productId, Product productToUpdate) {
        return inventoryService.updateProduct(productId, productToUpdate)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Integer id) {
        return inventoryService.deleteProduct(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .onErrorReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
