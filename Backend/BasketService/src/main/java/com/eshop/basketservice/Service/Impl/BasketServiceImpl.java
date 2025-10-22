package com.eshop.basketservice.Service.Impl;

import com.eshop.basketservice.Exception.BasketNotFoundException;
import com.eshop.basketservice.Model.Basket;
import com.eshop.basketservice.Repository.IBasketRepository;
import com.eshop.basketservice.Service.IBasketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BasketServiceImpl implements IBasketService {
    private final IBasketRepository IBasketRepository;

    @Override
    @Transactional
    public boolean updateBasket(Basket request) {
        boolean isUpdated = false;
        Optional<Basket> existingBasket = IBasketRepository.findById(request.getBuyerId());
        if (existingBasket.isEmpty()) {
            throw new BasketNotFoundException("Basket" , "BuyerId", request.getBuyerId());
        }
        IBasketRepository.save(request);
        log.info("Basket updated successfully");
        isUpdated = true;

        return  isUpdated;
    }




}
