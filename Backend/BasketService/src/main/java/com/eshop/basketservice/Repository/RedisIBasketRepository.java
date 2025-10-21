package com.eshop.basketservice.Repository;

import com.eshop.basketservice.Model.Basket;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Lớp cài đặt của BasketRepository, sử dụng Redis làm cơ sở dữ liệu.
 * Spring sẽ nhận diện lớp này là một Bean nhờ annotation @Repository.
 */
@Repository
@RequiredArgsConstructor
public class RedisIBasketRepository implements IBasketRepository {

    private final RedisTemplate<String, Basket> redisTemplate;
    private ValueOperations<String, Basket> valueOps;

    @PostConstruct
    private void init() {
        valueOps = redisTemplate.opsForValue();
    }

    @Override
    public Optional<Basket> findById(String buyerId) {
        Basket basket = valueOps.get(buyerId);
        return Optional.ofNullable(basket);
    }

    @Override
    public Basket save(Basket basket) {
        valueOps.set(basket.getBuyerId(), basket);
        return findById(basket.getBuyerId()).orElse(null);
    }

    @Override
    public void deleteById(String buyerId) {
        redisTemplate.delete(buyerId);
    }
}