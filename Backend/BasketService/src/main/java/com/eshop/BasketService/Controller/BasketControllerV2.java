package com.eshop.BasketService.Controller;

import com.eshop.BasketService.Constants.BasketConstants;
import com.eshop.BasketService.DTO.PaymentUrlResponseDto;
import com.eshop.BasketService.DTO.ResponseDto;
import com.eshop.BasketService.Model.Basket;
import com.eshop.BasketService.Model.BasketCheckout;
import com.eshop.BasketService.Repository.BasketRepository;
import com.eshop.BasketService.Service.IBasketService;
import com.eshop.BasketService.Service.IIdentityService;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/basket")
@RequiredArgsConstructor
public class BasketControllerV2 {
    private final IBasketService basketService;
    private final IIdentityService identityService;

    @PostMapping("/checkout")
    public ResponseEntity<PaymentUrlResponseDto> checkout(@RequestBody BasketCheckout basketCheckout,@RequestHeader(value = "X-Request-Id", required = false) String requestId) {
        String buyerId = identityService.getUserIdentity();
        ResponseEntity<PaymentUrlResponseDto> response = basketService.checkoutV2(buyerId, basketCheckout, requestId);
        return response;
    }
}