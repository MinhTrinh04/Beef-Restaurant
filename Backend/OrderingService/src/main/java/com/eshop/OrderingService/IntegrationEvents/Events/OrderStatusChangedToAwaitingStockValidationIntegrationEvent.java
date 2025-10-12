package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderStatusChangedToAwaitingStockValidationIntegrationEvent extends IntegrationEvent {
    private Long orderId;
    private List<OrderStockItem> orderStockItems;

    public OrderStatusChangedToAwaitingStockValidationIntegrationEvent() {
    }

    public OrderStatusChangedToAwaitingStockValidationIntegrationEvent(Long orderId,
            List<OrderStockItem> orderStockItems) {
        this.orderId = orderId;
        this.orderStockItems = orderStockItems;
    }
}
