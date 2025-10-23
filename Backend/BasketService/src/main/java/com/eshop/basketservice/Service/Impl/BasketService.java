package com.eshop.basketservice.Service.Impl;

import com.eshop.basketservice.Exception.BasketNotFoundException;
import com.eshop.basketservice.Model.Basket;
import com.eshop.basketservice.Repository.BasketRepository;
import com.eshop.basketservice.Service.IBasketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class BasketService implements IBasketService {
    private final BasketRepository basketRepository;


    @Override
    public Basket getBasketById (String id){
        Basket existingBasket = basketRepository.findById(id).orElseThrow(() -> new BasketNotFoundException("Basket", "buyerId", id));
        return existingBasket;
    }

    @Override
    @Transactional
    public boolean updateBasket (Basket basket){
        boolean isUpdated = false;
        Basket existingBasket = basketRepository.findById(basket.getBuyerId()).orElseThrow(() -> new BasketNotFoundException("Basket", "buyerId", basket.getBuyerId()));
        existingBasket.setItems(basket.getItems());
        basketRepository.save(basket);
        isUpdated = true;
        return isUpdated;
    }

    @Override
    @Transactional
    public boolean deleteBasket (String id){

        return true;
    }
}
