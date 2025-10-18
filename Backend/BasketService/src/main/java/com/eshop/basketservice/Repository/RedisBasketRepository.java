package com.eshop.basketservice.repository;

import com.eshop.basketservice.Model.Basket;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Lớp cài đặt của BasketRepository, sử dụng Redis làm cơ sở dữ liệu.
 * Spring sẽ nhận diện lớp này là một Bean nhờ annotation @Repository.
 */
@Repository
@RequiredArgsConstructor
public class RedisBasketRepository implements BasketRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final Gson gson;

    @Override
    public Optional<Basket> findById(String id) {
        // Lấy dữ liệu dạng JSON từ Redis bằng key là id người dùng
        var basketJson = redisTemplate.opsForValue().get(id);

        // Nếu không có dữ liệu, trả về Optional rỗng
        if (basketJson == null || basketJson.isEmpty()) {
            return Optional.empty();
        }

        // Chuyển đổi chuỗi JSON thành đối tượng Basket và trả về
        return Optional.of(gson.fromJson(basketJson, Basket.class));
    }

    @Override
    public Basket save(Basket basket) {
        // Chuyển đổi đối tượng Basket thành chuỗi JSON và lưu vào Redis
        redisTemplate.opsForValue().set(basket.getBuyerId(), gson.toJson(basket));
        return basket;
    }

    @Override
    public void deleteById(String id) {
        // Xóa giỏ hàng khỏi Redis bằng key là id người dùng
        redisTemplate.delete(id);
    }
}