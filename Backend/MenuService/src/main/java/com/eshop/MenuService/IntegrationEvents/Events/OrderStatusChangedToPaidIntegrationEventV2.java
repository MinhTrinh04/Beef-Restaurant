package com.eshop.MenuService.IntegrationEvents.Events;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderStatusChangedToPaidIntegrationEventV2 extends IntegrationEvent {
    private Long orderId;
    private List<OrderStockItem> orderStockItems;
}
