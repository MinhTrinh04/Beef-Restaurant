package com.eshop.BasketService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentUrlRequestDto {
    private UUID orderId;
    private BigDecimal amount;
    private String bankCode;
    private String language;
}