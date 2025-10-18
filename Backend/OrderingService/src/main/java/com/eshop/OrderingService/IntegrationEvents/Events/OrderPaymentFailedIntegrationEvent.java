package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

@Getter
public class OrderPaymentFailedIntegrationEvent extends IntegrationEvent {

    private final String orderId;
    private final String reason;

    public OrderPaymentFailedIntegrationEvent(String orderId, String reason) {
        super();
        this.orderId = orderId;
        this.reason = reason;
    }
}
