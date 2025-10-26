package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserCheckoutAcceptedIntegrationEvent extends IntegrationEvent {

    private final String userId;
    private final String userName;
    private final String city;
    private final String street;
    private final String state;
    private final String country;
    private final String zipCode;
    private final String cardNumber;
    private final String cardHolderName;
    private final String cardExpiration;
    private final String cardSecurityNumber;
    private final Integer cardTypeId;
    private final String buyer;
    private final String buyerEmail;
    private final List<BasketItem> basketItems;

}
