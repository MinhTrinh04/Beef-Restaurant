package com.eshop.basketservice.Model;

import lombok.Data;

@Data
public class BasketCheckout {
    private String userEmail;
    private String city;
    private String street;
    private String state;
    private String country;
}