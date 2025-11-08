package com.eshop.BasketService.Service.client;

import com.eshop.BasketService.DTO.CreatePaymentUrlRequestDto;
import com.eshop.BasketService.DTO.PaymentUrlResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentServiceClient {

    @PostMapping("/api/v1/payment/create-url")
    ResponseEntity<PaymentUrlResponseDto> createPaymentUrl(@RequestBody CreatePaymentUrlRequestDto requestDto);
}