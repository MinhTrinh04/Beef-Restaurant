package com.eshop.PaymentService.Controller;

import com.eshop.PaymentService.DTO.CreatePaymentUrlRequestDto;
import com.eshop.PaymentService.DTO.PaymentUrlResponseDto;
import com.eshop.PaymentService.Service.PayOSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PayOSService payOSService;

    // 1. API Tạo Link (Giữ nguyên signature để BasketService gọi)
    @PostMapping("/create-url")
    public ResponseEntity<PaymentUrlResponseDto> createPaymentUrl(
            @RequestBody CreatePaymentUrlRequestDto requestDto) {

        log.info("Received request to create PayOS URL for OrderId: {}", requestDto.getOrderId());

        if (requestDto.getOrderId() == null || requestDto.getAmount() == null) {
            return ResponseEntity.badRequest().body(new PaymentUrlResponseDto("99", "Invalid request data", null));
        }

        try {
            String paymentUrl = payOSService.createPaymentUrl(requestDto);
            PaymentUrlResponseDto responseDto = new PaymentUrlResponseDto("00", "Success", paymentUrl);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            log.error("Error creating PayOS URL for OrderId: {}", requestDto.getOrderId(), e);
            return ResponseEntity.internalServerError()
                    .body(new PaymentUrlResponseDto("99", "Error: " + e.getMessage(), null));
        }
    }

    // 2. Webhook PayOS (Mở public để PayOS gọi vào)
    @PostMapping("/payos-webhook")
    public ResponseEntity<String> handlePayOSWebhook(@RequestBody Object webhookBody) {
        log.info("Received PayOS Webhook Body: {}", webhookBody);

        try {
            payOSService.handleWebhook(webhookBody);
            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            // Trả về 200 kể cả khi lỗi logic để PayOS không retry spam server mình
            return ResponseEntity.ok("Webhook received but error: " + e.getMessage());
        }
    }

    // 3. Cancel URL - Redirect từ PayOS khi user ấn Cancel
    @GetMapping("/cancel")
    public ResponseEntity<String> handleCancelPayment(@RequestParam Long orderId) {
        log.info("❌ Payment CANCELLED by user for OrderId: {}", orderId);

        try {
            payOSService.handlePaymentCancelled(orderId);
            // Redirect về frontend payment cancelled page hoặc order detail
            return ResponseEntity.ok("Payment cancelled. OrderId: " + orderId);
        } catch (Exception e) {
            log.error("Error handling cancel payment for OrderId: {}: {}", orderId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error processing cancellation: " + e.getMessage());
        }
    }
}