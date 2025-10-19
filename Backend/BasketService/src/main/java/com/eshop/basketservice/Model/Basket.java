package com.eshop.basketservice.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Basket {
    private String buyerId;
    private List<BasketItem> items = new ArrayList<>();

    public Basket(String buyerId) {
        this.buyerId = buyerId;
    }
}