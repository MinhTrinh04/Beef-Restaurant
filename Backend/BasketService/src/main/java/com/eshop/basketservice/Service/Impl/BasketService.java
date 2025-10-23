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
    public Basket getBasketById(String id) {
        Basket existingBasket = basketRepository.findById(id).orElseThrow(() -> new BasketNotFoundException("Basket", "buyerId", id));
        return existingBasket;
    }

    @Override
    @Transactional
    public boolean updateBasket(Basket basket) {
        Basket resBasket = basketRepository.save(basket);
        if (resBasket == null){
            log.error("Error updating/creating basket for buyerId: {}", basket.getBuyerId());
            return false;
        }
        log.info("Basket updated/created successfully for buyerId: {}", basket.getBuyerId());
        return true;

    }

    @Override
    @Transactional
    public boolean deleteBasket(String id) {
        basketRepository.deleteById(id);
        return true;
    }
}
