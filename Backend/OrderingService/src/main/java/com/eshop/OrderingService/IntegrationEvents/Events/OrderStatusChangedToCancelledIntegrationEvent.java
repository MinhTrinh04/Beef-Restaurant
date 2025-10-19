package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

@Getter
public class OrderStatusChangedToCancelledIntegrationEvent extends IntegrationEvent {

    private final String orderId;
    private final String userId;
    private final String orderStatus;
    private final String reason;

    public OrderStatusChangedToCancelledIntegrationEvent(String orderId, String userId, String reason) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = "Cancelled";
        this.reason = reason;
    }
}
