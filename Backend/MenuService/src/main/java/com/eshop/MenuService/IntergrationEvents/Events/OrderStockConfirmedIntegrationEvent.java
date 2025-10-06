package com.eshop.MenuService.IntergrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderStockConfirmedIntegrationEvent extends IntegrationEvent {
    private UUID orderId;
}
