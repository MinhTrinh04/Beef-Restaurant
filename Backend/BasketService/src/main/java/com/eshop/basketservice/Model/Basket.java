package com.eshop.basketservice.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RedisHash("CustomerBasket")
public class Basket implements Serializable {
    private String buyerId;
    private List<BasketItem> items = new ArrayList<>();

    public Basket(String buyerId) {
        this.buyerId = buyerId;
    }
}