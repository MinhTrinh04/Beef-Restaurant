package com.eshop.basketservice.Integrationevents.Events;

import com.eshop.basketservice.Model.Basket;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckoutAcceptedIntegrationEvent extends IntegrationEvent {
    private String userId;
    private String userEmail;
    private String city;
    private String street;
    private String state;
    private String country;
    private UUID requestId;
    private Basket basket;
}