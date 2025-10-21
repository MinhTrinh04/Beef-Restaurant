//package com.eshop.basketservice.Service.Client;
//
//import com.eshop.basketservice.DTO.CreatePaymentUrlRequestDto;
//import com.eshop.basketservice.DTO.PaymentUrlResponseDto;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@FeignClient(name = "payment-service")
//public interface PaymentServiceClient {
//    @PostMapping("/api/v1/payment/create-url")
//    ResponseEntity<PaymentUrlResponseDto> createPaymentUrl(
//            @RequestBody CreatePaymentUrlRequestDto requestDto,
//            @RequestHeader("X-Forwarded-For") String ipAddress
//    );
//}
