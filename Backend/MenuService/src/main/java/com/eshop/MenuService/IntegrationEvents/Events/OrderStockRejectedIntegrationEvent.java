package com.eshop.MenuService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class OrderStockRejectedIntegrationEvent extends IntegrationEvent {
    private UUID orderId;
    private List<ConfirmedOrderStockItem> OrderStockItems;
}
