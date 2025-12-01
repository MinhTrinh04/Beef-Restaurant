package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UserCheckoutAcceptedIntegrationEventV2 extends IntegrationEvent {
    private final Long orderId;
    private final String userId;
    private final String userEmail;
    private final String city;
    private final String street;
    private final String state;
    private final String country;
    private final Basket basket;
    private final BigDecimal totalAmount;
}
