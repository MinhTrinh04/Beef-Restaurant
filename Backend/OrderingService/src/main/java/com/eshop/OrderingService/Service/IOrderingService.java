package com.eshop.OrderingService.Service;

import com.eshop.OrderingService.DTO.OrderDTO;
import java.util.List;

public interface IOrderingService {
    List<OrderDTO> getOrdersByBuyerId(String buyerId);

    OrderDTO getOrderById(Long orderId);

    void cancelOrder(Long orderId);

    void createOrder(OrderDTO orderDTO);
}
