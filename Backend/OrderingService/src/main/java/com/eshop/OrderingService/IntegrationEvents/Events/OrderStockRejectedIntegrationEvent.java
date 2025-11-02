package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class OrderStockRejectedIntegrationEvent extends IntegrationEvent {

    private final UUID orderId;
    private final List<ConfirmedOrderStockItem> confirmedOrderStockItems;

    public OrderStockRejectedIntegrationEvent(UUID orderId, List<ConfirmedOrderStockItem> confirmedOrderStockItems) {
        super();
        this.orderId = orderId;
        this.confirmedOrderStockItems = confirmedOrderStockItems;
    }
}
