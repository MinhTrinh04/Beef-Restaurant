package com.eshop.OrderingService.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentUrlRequestDto {
    private UUID orderId;
    private BigDecimal amount;
    private String bankCode;
    private String language;
}