package com.eshop.BasketService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentUrlResponseDto {
    private String code;
    private String message;
    private String paymentUrl;
}

