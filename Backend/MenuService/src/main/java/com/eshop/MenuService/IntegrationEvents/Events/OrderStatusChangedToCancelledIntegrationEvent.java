package com.eshop.MenuService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderStatusChangedToCancelledIntegrationEvent extends IntegrationEvent {

    private final UUID orderId;
    private final String userId;
    private final String orderStatus;
    private final String reason;
    private final List<OrderStockItem> orderItems;

}

