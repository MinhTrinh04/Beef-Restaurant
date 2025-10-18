package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Getter;

import java.util.List;

@Getter
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

    public UserCheckoutAcceptedIntegrationEvent(String userId, String userName, String city, String street,
            String state, String country, String zipCode, String cardNumber,
            String cardHolderName, String cardExpiration, String cardSecurityNumber,
            Integer cardTypeId, String buyer, String buyerEmail, List<BasketItem> basketItems) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.city = city;
        this.street = street;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardExpiration = cardExpiration;
        this.cardSecurityNumber = cardSecurityNumber;
        this.cardTypeId = cardTypeId;
        this.buyer = buyer;
        this.buyerEmail = buyerEmail;
        this.basketItems = basketItems;
    }
}
