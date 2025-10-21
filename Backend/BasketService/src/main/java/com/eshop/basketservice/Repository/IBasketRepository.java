package com.eshop.basketservice.Repository;

import com.eshop.basketservice.Model.Basket;
import java.util.Optional;

/**
 * Interface định nghĩa các hợp đồng (phương thức) cho việc truy cập dữ liệu giỏ hàng.
 * Bất kỳ lớp nào muốn hoạt động như một kho chứa giỏ hàng đều phải cài đặt interface này.
 */
public interface IBasketRepository {

    Optional<Basket> findById(String buyerId);

    Basket save(Basket basket);

    void deleteById(String userId);
}