package com.eshop.BasketService.Service.client;

import com.eshop.BasketService.Config.FeignClientConfig;
import com.eshop.BasketService.DTO.CreateOrderFromBasketRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "ordering-service", configuration = FeignClientConfig.class)
public interface OrderingServiceClient {

    @PostMapping("/api/v1/orders/create-from-basket")
    ResponseEntity<UUID> createOrderFromBasket(@RequestBody CreateOrderFromBasketRequestDto request);
}

