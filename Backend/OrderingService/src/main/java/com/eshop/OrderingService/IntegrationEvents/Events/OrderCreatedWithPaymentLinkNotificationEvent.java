package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreatedWithPaymentLinkNotificationEvent extends IntegrationEvent {
    private Long orderId;
    private String paymentUrl;
}
