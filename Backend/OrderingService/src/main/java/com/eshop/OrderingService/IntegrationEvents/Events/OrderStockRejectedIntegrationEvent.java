package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderStockRejectedIntegrationEvent extends IntegrationEvent {

    private final String orderId;
    private final List<ConfirmedOrderStockItem> confirmedOrderStockItems;

    public OrderStockRejectedIntegrationEvent(String orderId, List<ConfirmedOrderStockItem> confirmedOrderStockItems) {
        super();
        this.orderId = orderId;
        this.confirmedOrderStockItems = confirmedOrderStockItems;
    }
}
