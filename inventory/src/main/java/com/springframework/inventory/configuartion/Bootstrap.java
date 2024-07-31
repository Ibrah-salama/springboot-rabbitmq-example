package com.springframework.inventory.configuartion;

import com.springframework.inventory.domain.Product;
import com.springframework.inventory.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final ProductRepository productRepository;

        @Override
        public void run(String... args) throws Exception {
            Product iphone15 = new Product();
            iphone15.setName("Iphone 15 pro max");
            iphone15.setQuantity(50);
            Product raspberryPi = new Product();
            raspberryPi.setName("Raspberry pi 5");
            raspberryPi.setQuantity(50);
            Product iphone13 = new Product();
            iphone13.setName("Iphone 13 pro");
            iphone13.setQuantity(50);
            List<Product> products = List.of(
                    iphone15,
                    raspberryPi,
                    iphone13
            );
            productRepository.saveAll(products);
        }
}
