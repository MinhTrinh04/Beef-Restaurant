package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserCheckoutAcceptedIntegrationEvent extends IntegrationEvent {
    private final String userId;
//    private final String userEmail;
//    private final String city;
//    private final String street;
//    private final String state;
//    private final String country;
//    private final UUID requestId;
    private final Basket basket;
}
