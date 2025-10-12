package com.eshop.MenuService.IntergrationEvents.Events;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class OrderStatusChangedToAwaitingStockValidationIntegrationEvent extends IntegrationEvent{
    private UUID orderId;
    private List<OrderStockItem> orderStockItems;

}
