package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {

    private final UUID orderId;
    private final String userId;
    private final String orderStatus;

    public OrderStatusChangedToPaidIntegrationEvent(UUID orderId, String userId) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = "Paid";
    }
}
