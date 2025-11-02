package com.eshop.PaymentService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VNPayCallbackResponseDto {
    private String RspCode;
    private String Message;
}