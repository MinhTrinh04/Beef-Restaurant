package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderStatusChangedToCancelledIntegrationEvent extends IntegrationEvent {

    private final Long orderId;
    private final String userId;
    private final String orderStatus;
    private final String reason;
    private final List<OrderStockItem> orderItems;

    public OrderStatusChangedToCancelledIntegrationEvent(Long orderId, String userId, String reason, List<OrderStockItem> orderItems) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = "Cancelled";
        this.reason = reason;
        this.orderItems = orderItems;
    }
}
