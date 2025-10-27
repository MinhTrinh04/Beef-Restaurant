package com.eshop.BasketService.Repository;

import com.eshop.BasketService.Model.Basket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisBasketRepository implements BasketRepository {

    private final RedisTemplate<String, Basket> redisTemplate;
    // Ko hash -> sang ValueOperations
    private ValueOperations<String, Basket> valueOperations;

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public Optional<Basket> findById(String buyerId) {
        Basket basket = valueOperations.get(buyerId);
        return Optional.ofNullable(basket);
    }

    @Override
    public Basket save(Basket basket) {
        valueOperations.set(basket.getBuyerId(), basket);
        return findById(basket.getBuyerId()).orElse(null);
    }

    @Override
    public void deleteById(String buyerId) {
        redisTemplate.delete(buyerId);
    }
}