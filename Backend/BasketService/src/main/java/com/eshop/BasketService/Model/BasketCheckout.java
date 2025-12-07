package com.eshop.BasketService.Model;

import lombok.Data;

@Data
public class BasketCheckout {
    private String city;
    private String street;
    private String state;
    private String country;
}