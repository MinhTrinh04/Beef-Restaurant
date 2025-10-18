package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderStatusChangedToSubmittedIntegrationEvent extends IntegrationEvent {

    private final String orderId;
    private final String userId;
    private final String orderStatus;

    public OrderStatusChangedToSubmittedIntegrationEvent(String orderId, String userId) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = "Submitted";
    }
}
