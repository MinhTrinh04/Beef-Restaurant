package com.eshop.OrderingService.Infrastructure.Repository;

import com.eshop.OrderingService.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerId(String buyerId);
}
