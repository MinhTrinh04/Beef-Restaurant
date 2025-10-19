package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

@Getter
public class OrderPaymentSucceededIntegrationEvent extends IntegrationEvent {

    private final String orderId;

    public OrderPaymentSucceededIntegrationEvent(String orderId) {
        super();
        this.orderId = orderId;
    }
}
