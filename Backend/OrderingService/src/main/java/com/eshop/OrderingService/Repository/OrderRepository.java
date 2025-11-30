package com.eshop.OrderingService.Repository;

import com.eshop.OrderingService.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(Long orderId);

    List<Order> findByBuyerId(String userId);

    List<Order> findByOrderStatus(String orderStatus);

    List<Order> findByOrderStatusAndUpdatedAtBefore(String orderStatus, LocalDateTime lastModifiedDate);

    @Query("SELECT o FROM Order o WHERE o.buyerId = :userId ORDER BY o.orderDate DESC")
    List<Order> findByUserIdOrderByOrderDateDesc(@Param("userId") String userId);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status ORDER BY o.orderDate DESC")
    List<Order> findByOrderStatusOrderByOrderDateDesc(@Param("status") String status);

    boolean existsByOrderId(Long orderId);
}
