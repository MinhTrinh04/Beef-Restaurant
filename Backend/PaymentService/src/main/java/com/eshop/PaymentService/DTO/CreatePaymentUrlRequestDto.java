package com.eshop.PaymentService.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreatePaymentUrlRequestDto {
    private Long orderId;
    private BigDecimal amount;
    private String bankCode;
    private String language;
}