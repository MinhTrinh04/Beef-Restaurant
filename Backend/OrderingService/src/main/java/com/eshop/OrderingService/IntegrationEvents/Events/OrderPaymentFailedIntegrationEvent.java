package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderPaymentFailedIntegrationEvent extends IntegrationEvent {

    private final UUID orderId;
    private final String reason;

    public OrderPaymentFailedIntegrationEvent(UUID orderId, String reason) {
        super();
        this.orderId = orderId;
        this.reason = reason;
    }
}
