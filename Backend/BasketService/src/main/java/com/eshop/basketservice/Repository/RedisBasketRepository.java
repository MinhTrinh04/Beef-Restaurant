package com.eshop.basketservice.Repository;

import com.eshop.basketservice.Model.Basket;
import com.eshop.basketservice.Model.BasketItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisBasketRepository implements BasketRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper; // Sử dụng Jackson ObjectMapper của Spring

    private static final String HASH_KEY_PREFIX = "basket:";

    @Override
    public Optional<Basket> findById(String id) {
        String key = HASH_KEY_PREFIX + id;
        log.info("Finding basket with key: {}", key);

        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            log.warn("Basket with key '{}' not found in Redis.", key);
            return Optional.empty();
        }

        Map<Object, Object> rawHash = redisTemplate.opsForHash().entries(key);
        log.info("Found raw hash for key '{}': {}", key, rawHash);

        // Chuyển đổi từ Map<Object, Object> (thường là Map<String, String>) sang Basket
        try {
            Basket basket = objectMapper.convertValue(rawHash, Basket.class);
            return Optional.of(basket);
        } catch (Exception e) {
            log.error("Error converting Redis hash to Basket for key '{}'", key, e);
            return Optional.empty();
        }
    }

    @Override
    public Basket save(Basket basket) {
        String key = HASH_KEY_PREFIX + basket.getBuyerId();
        log.info("Attempting to save basket to Redis with key: {}", key);

        try {
            // Chuyển đổi Basket object thành Map để lưu dưới dạng Hash trong Redis
            Map<String, Object> basketMap = objectMapper.convertValue(basket, Map.class);

            log.info("Converted basket to map: {}", basketMap);

            redisTemplate.opsForHash().putAll(key, basketMap);
            log.info("Successfully called putAll for key: {}", key);

        } catch (Exception e) {
            log.error("Exception caught while saving to Redis for key: {}", key, e);
        }
        return basket;
    }

    @Override
    public void deleteById(String id) {
        String key = HASH_KEY_PREFIX + id;
        log.info("Deleting basket with key: {}", key);
        redisTemplate.delete(key);
    }
}