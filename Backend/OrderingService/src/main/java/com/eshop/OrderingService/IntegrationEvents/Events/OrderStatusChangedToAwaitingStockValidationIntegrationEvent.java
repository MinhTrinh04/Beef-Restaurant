package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderStatusChangedToAwaitingStockValidationIntegrationEvent extends IntegrationEvent {

    private final String orderId;
    private final String userId;
    private final String orderStatus;
    private final List<OrderStockItem> orderStockItems;

    public OrderStatusChangedToAwaitingStockValidationIntegrationEvent(String orderId, String userId,
            List<OrderStockItem> orderStockItems) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = "AwaitingStockValidation";
        this.orderStockItems = orderStockItems;
    }
}
