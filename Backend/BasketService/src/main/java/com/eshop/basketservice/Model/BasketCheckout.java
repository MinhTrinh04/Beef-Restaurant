package com.eshop.basket.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BasketCheckout {
    private String city;
    private String street;
    private String state;
    private String country;
    private String zipCode;
    private String cardNumber;
    private String cardHolderName;
    private String cardExpiration;
    private String cardSecurityNumber;
    private int cardTypeId;
    private String buyer;
    private BigDecimal orderTotal;
}