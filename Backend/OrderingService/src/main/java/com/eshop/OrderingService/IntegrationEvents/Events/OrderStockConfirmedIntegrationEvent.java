package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

@Getter
public class OrderStockConfirmedIntegrationEvent extends IntegrationEvent {

    private final String orderId;

    public OrderStockConfirmedIntegrationEvent(String orderId) {
        super();
        this.orderId = orderId;
    }
}
