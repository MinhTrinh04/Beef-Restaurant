package com.eshop.PaymentService.Controller;

import com.eshop.PaymentService.DTO.CreatePaymentUrlRequestDto;
import com.eshop.PaymentService.DTO.PaymentUrlResponseDto;
import com.eshop.PaymentService.DTO.VNPayCallbackResponseDto;
import com.eshop.PaymentService.Service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final VNPayService vnPayService;

    @PostMapping("/create-url")
    public ResponseEntity<PaymentUrlResponseDto> createPaymentUrl(
            @RequestBody CreatePaymentUrlRequestDto requestDto,
            HttpServletRequest httpServletRequest) {

        log.info("Received request to create payment URL for OrderId: {}", requestDto.getOrderId());
        if (requestDto.getOrderId() == null || requestDto.getAmount() == null || requestDto.getAmount().signum() <= 0) {
            log.warn("Invalid request data for create payment URL.");
            return ResponseEntity.badRequest().body(new PaymentUrlResponseDto("99", "Invalid request data", null));
        }

        try {
            String paymentUrl = vnPayService.createPaymentUrl(requestDto, httpServletRequest);
            log.info("Successfully created payment URL for OrderId: {}", requestDto.getOrderId());
            PaymentUrlResponseDto responseDto = new PaymentUrlResponseDto("00", "Success", paymentUrl);
            return ResponseEntity.ok(responseDto);
        } catch (UnsupportedEncodingException e) {
            log.error("Error creating payment URL for OrderId: {}", requestDto.getOrderId(), e);
            PaymentUrlResponseDto responseDto = new PaymentUrlResponseDto("99", "Error creating payment URL", null);

            return ResponseEntity.internalServerError().body(responseDto);
        } catch (Exception e) {
            log.error("Unexpected error creating payment URL for OrderId: {}", requestDto.getOrderId(), e);
            PaymentUrlResponseDto responseDto = new PaymentUrlResponseDto("99", "Unexpected error", null);
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


    @GetMapping("/vnpay-return")
    public ResponseEntity<VNPayCallbackResponseDto> handleVNPayCallback(HttpServletRequest request) {
        log.info("Received VNPay callback request.");
        VNPayCallbackResponseDto response = vnPayService.handleVNPayCallback(request);
        log.info("Responding to VNPay callback with: {}", response);

        return ResponseEntity.ok(response);
    }
}