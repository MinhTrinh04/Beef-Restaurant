package com.eshop.BasketService.Service;

import com.eshop.BasketService.Model.Basket;
import com.eshop.BasketService.Model.BasketCheckout;


public interface IBasketService {
    //Get
    Basket getBasketById (String id);

    //Update
    boolean updateBasket (Basket basket);

    //Delete
    boolean deleteBasket (String id);

    void checkout(String id, BasketCheckout basket, String requestId);
}
