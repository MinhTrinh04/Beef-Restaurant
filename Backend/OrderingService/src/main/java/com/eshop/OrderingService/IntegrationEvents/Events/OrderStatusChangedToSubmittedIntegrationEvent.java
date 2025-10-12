package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderStatusChangedToSubmittedIntegrationEvent extends IntegrationEvent {
    private Long orderId;
    private String buyerId;

    public OrderStatusChangedToSubmittedIntegrationEvent() {
    }

    public OrderStatusChangedToSubmittedIntegrationEvent(Long orderId, String buyerId) {
        this.orderId = orderId;
        this.buyerId = buyerId;
    }
}
