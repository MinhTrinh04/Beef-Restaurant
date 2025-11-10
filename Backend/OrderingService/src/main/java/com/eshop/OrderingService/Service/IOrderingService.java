package com.eshop.OrderingService.Service;

import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.IntegrationEvents.Events.UserCheckoutAcceptedIntegrationEventV2;

import java.util.List;
import java.util.UUID;

public interface IOrderingService {

    // Order Management
//    OrderDto createOrder(CreateOrderRequestDto request);

    OrderDto getOrderById(Long id);

    OrderDto getOrderByOrderId(UUID orderId);

    List<OrderDto> getOrdersByUserId(String userId);

    List<OrderDto> getAllOrders();

    // Order Status Management
    boolean cancelOrder(UUID orderId, String reason);
    // Integration Event Handlers
    void createOrderFromCheckoutV2(UserCheckoutAcceptedIntegrationEventV2 event);

    void updateOrderStatusToPaidV2(UUID orderId);


}
