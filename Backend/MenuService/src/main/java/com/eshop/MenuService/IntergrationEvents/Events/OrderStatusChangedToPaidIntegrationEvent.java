package com.eshop.MenuService.IntergrationEvents.Events;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {
    private int orderId;
    private List<OrderStockItem> orderStockItems;
}
