package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderStatusChangedToPaidIntegrationEventV2 extends IntegrationEvent {

    private final Long orderId;
    private final String userId;
    private final String orderStatus;
    private List<OrderStockItem> orderStockItems;

    public OrderStatusChangedToPaidIntegrationEventV2(Long orderId, String userId, List<OrderStockItem> orderStockItems) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = "Paid";
        this.orderStockItems = orderStockItems;
    }
}
