package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderStatusChangedToCancelledIntegrationEvent extends IntegrationEvent {
    private Long orderId;

    public OrderStatusChangedToCancelledIntegrationEvent() {
    }

    public OrderStatusChangedToCancelledIntegrationEvent(Long orderId) {
        this.orderId = orderId;
    }
}
