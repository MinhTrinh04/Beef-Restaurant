package com.eshop.basketservice.Integrationevents.Events;

import com.eshop.basketservice.Model.Basket;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckoutAcceptedIntegrationEvent extends IntegrationEvent {
    private String userId;
    private String cardNumber;
    private String cardHolderName;
    private String cardExpiration;
    private String cardSecurityNumber;
    private int cardTypeId;
    private Basket basket;
    private BigDecimal orderTotal;
}