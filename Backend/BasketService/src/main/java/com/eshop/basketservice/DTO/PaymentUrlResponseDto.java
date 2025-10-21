package com.eshop.basketservice.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUrlResponseDto {
    private String code;
    private String message;
    private String paymentUrl;
}
