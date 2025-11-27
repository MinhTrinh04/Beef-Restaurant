package com.eshop.BasketService.Service.client;

import com.eshop.BasketService.Config.FeignClientConfig;
import com.eshop.BasketService.DTO.StockValidationItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "menu-service", configuration = FeignClientConfig.class)
public interface MenuServiceClient {

    @PostMapping("/api/v1/menu/validate-stock")
    ResponseEntity<Void> validateStock(@RequestBody List<StockValidationItem> items);
}
