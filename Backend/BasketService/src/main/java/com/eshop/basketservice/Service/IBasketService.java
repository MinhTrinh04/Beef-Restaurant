package com.eshop.basketservice.Service;

import com.eshop.basketservice.Model.Basket;

public interface IBasketService {
    //Get
    Basket getBasketById (String id);

    //Update
    boolean updateBasket (Basket basket);

    //Delete
    boolean deleteBasket (String id);

}
