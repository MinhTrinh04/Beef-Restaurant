package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserCheckoutAcceptedIntegrationEvent extends IntegrationEvent {
    private String userId;
    private String userName;
    private String city;
    private String street;
    private String state;
    private String country;
    private String zipCode;
    private String cardNumber;
    private String cardHolderName;
    private String cardExpiration;
    private String cardSecurityNumber;
    private String cardTypeId;
    private String buyer;
    private String requestId;
    private List<BasketItem> basketItems;

    public UserCheckoutAcceptedIntegrationEvent() {
    }

    public UserCheckoutAcceptedIntegrationEvent(String userId, String userName, String city, String street,
            String state, String country, String zipCode, String cardNumber,
            String cardHolderName, String cardExpiration, String cardSecurityNumber,
            String cardTypeId, String buyer, String requestId, List<BasketItem> basketItems) {
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
        this.requestId = requestId;
        this.basketItems = basketItems;
    }

    @Data
    public static class BasketItem {
        private String id;
        private String productId;
        private String productName;
        private BigDecimal unitPrice;
        private Double oldUnitPrice;
        private Integer quantity;
        private String pictureUrl;

        public BasketItem() {
        }

        public BasketItem(String id, String productId, String productName, BigDecimal unitPrice,
                Double oldUnitPrice, Integer quantity, String pictureUrl) {
            this.id = id;
            this.productId = productId;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.oldUnitPrice = oldUnitPrice;
            this.quantity = quantity;
            this.pictureUrl = pictureUrl;
        }
    }
}
