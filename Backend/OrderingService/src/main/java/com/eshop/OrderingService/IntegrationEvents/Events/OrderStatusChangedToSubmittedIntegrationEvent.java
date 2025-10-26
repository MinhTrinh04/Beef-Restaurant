package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class OrderStatusChangedToSubmittedIntegrationEvent extends IntegrationEvent {

    private final UUID orderId;
    private final String buyerId;
    private final String orderStatus;
    private final String buyerEmail;

    public OrderStatusChangedToSubmittedIntegrationEvent(UUID orderId, String buyerId, String buyerEmail) {
        super();
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.orderStatus = "Submitted";
        this.buyerEmail = buyerEmail;
    }
}
