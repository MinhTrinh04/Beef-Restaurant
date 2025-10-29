package com.eshop.BasketService.IntegrationEvents.Events;

import com.eshop.BasketService.Model.Basket;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPlaceOrderIntegrationEvent extends IntegrationEvent {
    private final String userId;
    private final Basket basket;
}