package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderPaymentSucceededIntegrationEvent extends IntegrationEvent {

    private final UUID orderId;

    public OrderPaymentSucceededIntegrationEvent(UUID orderId) {
        super();
        this.orderId = orderId;
    }
}
