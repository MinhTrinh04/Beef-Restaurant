package com.eshop.OrderingService.Service.Impl;

import com.eshop.OrderingService.DTO.OrderDTO;
import com.eshop.OrderingService.Infrastructure.Repository.OrderRepository;
import com.eshop.OrderingService.Mapper.OrderMapper;
import com.eshop.OrderingService.Model.Order;
import com.eshop.OrderingService.Model.OrderStatus;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import com.eshop.OrderingService.IntegrationEvents.Events.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderingServiceImpl implements IOrderingService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IEventBus eventBus;

    @Override
    public List<OrderDTO> getOrdersByBuyerId(String buyerId) {
        List<Order> orders = orderRepository.findByBuyerId(buyerId);
        return orders.stream()
                .map(orderMapper::mapToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.mapToOrderDTO(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getOrderStatus() == OrderStatus.Completed ||
                order.getOrderStatus() == OrderStatus.Shipped) {
            throw new RuntimeException("Cannot cancel completed or shipped order");
        }

        order.setOrderStatus(OrderStatus.Cancelled);
        orderRepository.save(order);

        eventBus.publish(new OrderStatusChangedToCancelledIntegrationEvent(orderId));
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.mapToOrder(orderDTO, new Order());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.Submitted);

        Order savedOrder = orderRepository.save(order);

        // Publish events
        eventBus.publish(
                new OrderStatusChangedToSubmittedIntegrationEvent(savedOrder.getId(), savedOrder.getBuyerId()));
        eventBus.publish(new OrderStatusChangedToAwaitingStockValidationIntegrationEvent(
                savedOrder.getId(),
                savedOrder.getOrderItems().stream()
                        .map(item -> new OrderStockItem(item.getProductId(), item.getUnits()))
                        .collect(Collectors.toList())));
    }
}
