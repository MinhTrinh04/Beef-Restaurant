package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

@Getter
public class OrderStatusChangedToValidatedIntegrationEvent extends IntegrationEvent {

    private final String orderId;
    private final String userId;
    private final String orderStatus;

    public OrderStatusChangedToValidatedIntegrationEvent(String orderId, String userId) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = "Validated";
    }
}
