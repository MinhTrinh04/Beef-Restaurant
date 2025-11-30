package com.eshop.PaymentService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderPaymentFailedIntegrationEvent extends IntegrationEvent {
    private Long orderId;
    private String reason;
}
