package com.eshop.BasketService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class BasketClearedIntegrationEvent extends IntegrationEvent {

    private final String buyerId;
}