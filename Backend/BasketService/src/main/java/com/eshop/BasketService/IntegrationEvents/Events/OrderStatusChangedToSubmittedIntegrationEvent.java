package com.eshop.BasketService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderStatusChangedToSubmittedIntegrationEvent extends IntegrationEvent {

    private final UUID orderId;
    private final String buyerId;
    private final String orderStatus;
    private final String buyerEmail;
}