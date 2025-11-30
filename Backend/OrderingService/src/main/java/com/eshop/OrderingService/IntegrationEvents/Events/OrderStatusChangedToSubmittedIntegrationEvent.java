package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderStatusChangedToSubmittedIntegrationEvent extends IntegrationEvent {

    private final Long orderId;
    private final String buyerId;
    private final String orderStatus;
    private final String buyerEmail;

    public OrderStatusChangedToSubmittedIntegrationEvent(Long orderId, String buyerId, String buyerEmail) {
        super();
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.orderStatus = "Submitted";
        this.buyerEmail = buyerEmail;
    }
}
