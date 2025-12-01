package com.eshop.BasketService.Service;

import com.eshop.BasketService.DTO.PaymentUrlResponseDto;
import com.eshop.BasketService.Model.Basket;
import com.eshop.BasketService.Model.BasketCheckout;
import org.springframework.http.ResponseEntity;


public interface IBasketService {
    //Get
    Basket getBasketById (String id);

    //Update
    boolean updateBasket (Basket basket);

    //Delete
    boolean deleteBasket (String id);

    //Checkout V2
    ResponseEntity<PaymentUrlResponseDto> checkoutV2(String id, BasketCheckout basketCheckout, String requestId);
}
