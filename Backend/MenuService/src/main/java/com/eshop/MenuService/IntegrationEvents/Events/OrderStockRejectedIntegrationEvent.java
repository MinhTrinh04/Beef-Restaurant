package com.eshop.MenuService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderStockRejectedIntegrationEvent extends IntegrationEvent {
    private UUID orderId;
    private List<ConfirmedOrderStockItem> OrderStockItems;
}
