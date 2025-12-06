package com.eshop.UserService.IntegrationEvents.Events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentSucceededIntegrationEvent {

    private Long orderId;
    private String email;
    private String customerName;
    private Double totalAmount;
    private String qrCodeUrl;
    private String orderDetails;
}
