package com.eshop.BasketService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// Thêm dòng này để Spring quét cả 2 package
@ComponentScan(basePackages = {"com.eshop.BasketService", "com.eshop.buildingblocks"})
public class BasketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasketServiceApplication.class, args);
    }

}