package com.eshop.basketservice.Service;

import com.eshop.basketservice.Model.Basket;
import com.eshop.basketservice.Model.BasketCheckout;


public interface IBasketService {
    //Get
    Basket getBasketById (String id);

    //Update
    boolean updateBasket (Basket basket);

    //Delete
    boolean deleteBasket (String id);

    void checkout(String id, BasketCheckout basket, String requestId);
}
