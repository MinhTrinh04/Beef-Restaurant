package com.eshop.OrderingService.Repository;

import com.eshop.OrderingService.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findByOrderOrderId(@Param("orderId") String orderId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.productId = :productId")
    List<OrderItem> findByProductId(@Param("productId") Integer productId);
}
