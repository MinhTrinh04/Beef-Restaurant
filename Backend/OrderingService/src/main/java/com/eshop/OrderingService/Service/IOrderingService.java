package com.eshop.OrderingService.Service;

import com.eshop.OrderingService.DTO.CreateOrderRequestDto;
import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.IntegrationEvents.Events.UserCheckoutAcceptedIntegrationEvent;

import java.util.List;

public interface IOrderingService {

    // Order Management
//    OrderDto createOrder(CreateOrderRequestDto request);

    OrderDto getOrderById(Long id);

    OrderDto getOrderByOrderId(String orderId);

    List<OrderDto> getOrdersByUserId(String userId);

    List<OrderDto> getAllOrders();

    // Order Status Management
    boolean cancelOrder(String orderId, String reason);

    boolean shipOrder(String orderId);

    // Integration Event Handlers
    void createOrderFromCheckout(UserCheckoutAcceptedIntegrationEvent event);

    void updateOrderStatusToValidated(String orderId);

    void updateOrderStatusToPaid(String orderId);

    void processOrderSubmission(String orderId);
}
