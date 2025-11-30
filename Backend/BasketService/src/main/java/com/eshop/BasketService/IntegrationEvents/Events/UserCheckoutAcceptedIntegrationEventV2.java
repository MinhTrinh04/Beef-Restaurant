package com.eshop.BasketService.IntegrationEvents.Events;

import com.eshop.BasketService.Model.Basket;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UserCheckoutAcceptedIntegrationEventV2 extends IntegrationEvent {
    private Long orderId;
    private final String userId;
    private final String userEmail;
    private final String city;
    private final String street;
    private final String state;
    private final String country;
    private final Basket basket;
    private final BigDecimal totalAmount;
}