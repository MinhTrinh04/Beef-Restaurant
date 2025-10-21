package com.eshop.basketservice.DTO;

import lombok.Data;

@Data
public class BasketCheckout {
    private String city;
    private String street;
    private String state;
    private String country;
    private String userEmail;
}