package com.eshop.BasketService.Integrationevents.Events;

import com.eshop.BasketService.Model.Basket;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserCheckoutAcceptedIntegrationEvent extends IntegrationEvent {
    private final String userId;
    private final String userEmail;
    private final String city;
    private final String street;
    private final String state;
    private final String country;
    private final UUID requestId;
    private final Basket basket;
}