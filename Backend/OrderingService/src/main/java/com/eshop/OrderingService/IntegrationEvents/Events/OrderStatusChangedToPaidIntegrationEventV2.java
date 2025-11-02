package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class OrderStatusChangedToPaidIntegrationEventV2 extends IntegrationEvent {

    private final UUID orderId;
    private final String userId;
    private final String orderStatus;
    private List<OrderStockItem> orderStockItems;

    public OrderStatusChangedToPaidIntegrationEventV2(UUID orderId, String userId, List<OrderStockItem> orderStockItems) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = "Paid";
        this.orderStockItems = orderStockItems;
    }
}
