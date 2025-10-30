package com.eshop.OrderingService.Service.Client;


import com.eshop.OrderingService.DTO.CreatePaymentUrlRequestDto;
import com.eshop.OrderingService.DTO.PaymentUrlResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentServiceClient {

    @PostMapping("/api/payment/url")
    ResponseEntity<PaymentUrlResponseDto> createPaymentUrl(@RequestBody CreatePaymentUrlRequestDto requestDto);
}