package com.eshop.OrderingService.Service;

import com.eshop.OrderingService.DTO.CreateOrderFromBasketRequestDto;
import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.IntegrationEvents.Events.UserCheckoutAcceptedIntegrationEventV2;

import java.util.List;

public interface IOrderingService {

    // Order Management
    Long createOrderFromBasket(CreateOrderFromBasketRequestDto request);

    OrderDto getOrderById(Long id);

    OrderDto getOrderByOrderId(Long orderId);

    List<OrderDto> getOrdersByUserId(String userId);

    List<OrderDto> getAllOrders();

    // Order Status Management
    boolean cancelOrder(Long orderId, String reason);
    // Integration Event Handlers
    void createOrderFromCheckoutV2(UserCheckoutAcceptedIntegrationEventV2 event);

    void updateOrderStatusToPaidV2(Long orderId);


}
