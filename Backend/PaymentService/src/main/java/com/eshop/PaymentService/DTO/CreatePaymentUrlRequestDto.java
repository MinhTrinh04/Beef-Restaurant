package com.eshop.PaymentService.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentUrlRequestDto {
    private UUID orderId;
    private BigDecimal amount;
    private String bankCode;
    private String language;
}