package com.eshop.OrderingService.Service;

import com.eshop.OrderingService.DTO.CreateOrderRequestDto;
import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.IntegrationEvents.Events.UserCheckoutAcceptedIntegrationEvent;
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

    boolean shipOrder(UUID orderId);

    // Integration Event Handlers
    void createOrderFromCheckout(UserCheckoutAcceptedIntegrationEvent event);

    void createOrderFromCheckoutV2(UserCheckoutAcceptedIntegrationEventV2 event);

    void updateOrderStatusToValidated(UUID orderId);

    void updateOrderStatusToPaid(UUID orderId);

    void updateOrderStatusToPaidV2(UUID orderId);

    void processOrderSubmission(UUID orderId);

    void processOrderSubmissionV2(UUID orderId);
}
