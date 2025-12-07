package com.eshop.PaymentService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Notification event from PaymentService when order payment link is created
 * Sent to OrderingService which then enriches and forwards to UserService
 */
@Getter
@AllArgsConstructor
public class OrderCreatedWithPaymentLinkNotificationEvent extends IntegrationEvent {
    private Long orderId;
    private String paymentUrl;
}
