package com.eshop.BasketService.Service.client;

import com.eshop.BasketService.DTO.StockValidationItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "MENU-SERVICE")
public interface MenuServiceClient {

    @PostMapping("/api/v2/menu/validate-stock")
    ResponseEntity<Void> validateStock(@RequestBody List<StockValidationItem> items);
}
