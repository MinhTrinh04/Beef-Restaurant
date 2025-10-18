package com.eshop.basketservice.repository;

import com.eshop.basketservice.model.Basket;
import java.util.Optional;

/**
 * Interface định nghĩa các hợp đồng (phương thức) cho việc truy cập dữ liệu giỏ hàng.
 * Bất kỳ lớp nào muốn hoạt động như một kho chứa giỏ hàng đều phải cài đặt interface này.
 */
public interface BasketRepository {

    /**
     * Tìm kiếm một giỏ hàng dựa trên ID của người dùng (buyerId).
     * @param id ID của người dùng.
     * @return Một đối tượng Optional chứa giỏ hàng nếu tìm thấy, ngược lại trả về Optional rỗng.
     */
    Optional<Basket> findById(String id);

    /**
     * Lưu hoặc cập nhật một giỏ hàng vào kho chứa.
     * @param basket Đối tượng giỏ hàng cần lưu.
     * @return Đối tượng giỏ hàng sau khi đã được lưu.
     */
    Basket save(Basket basket);

    /**
     * Xóa một giỏ hàng khỏi kho chứa dựa trên ID của người dùng.
     * @param id ID của người dùng có giỏ hàng cần xóa.
     */
    void deleteById(String id);
}