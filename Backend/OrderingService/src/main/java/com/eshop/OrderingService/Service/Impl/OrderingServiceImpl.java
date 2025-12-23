package com.eshop.OrderingService.Service.Impl;

import com.eshop.OrderingService.Constants.OrderingConstants;
import com.eshop.OrderingService.DTO.CreateOrderFromBasketRequestDto;
import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.Exception.InvalidOrderStatusException;
import com.eshop.OrderingService.Exception.OrderNotFoundException;
import com.eshop.OrderingService.IntegrationEvents.Events.*;
import com.eshop.OrderingService.Mapper.OrderMapper;
import com.eshop.OrderingService.Model.Order;
import com.eshop.OrderingService.Model.OrderItem;
import com.eshop.OrderingService.Repository.OrderRepository;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OrderingServiceImpl implements IOrderingService {

        private final OrderRepository orderRepository;
        private final OrderMapper orderMapper;
        private final IEventBus eventBus;

        private Long generateOrderId() {
                String millis = String.valueOf(System.currentTimeMillis());
                String last8 = millis.substring(Math.max(0, millis.length() - 8));
                return Long.parseLong(last8);
        }

        @Override
        public OrderDto getOrderById(Long id) {
                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new OrderNotFoundException("Order", "id", id.toString()));
                return orderMapper.toDto(order);
        }

        @Override
        public OrderDto getOrderByOrderId(Long orderId) {
                Order order = orderRepository.findByOrderId(orderId)
                                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId.toString()));
                return orderMapper.toDto(order);
        }

        @Override
        public List<OrderDto> getOrdersByUserId(String userId) {
                List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
                return orders.stream()
                                .map(orderMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        public List<OrderDto> getAllOrders() {
                List<Order> orders = orderRepository.findAll();
                return orders.stream()
                                .map(orderMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public Long createOrderFromBasket(CreateOrderFromBasketRequestDto request) {
                log.info("Creating order from basket for user: {}", request.getUserId());

                Order order = new Order();
                order.setOrderId(generateOrderId());
                order.setOrderDate(LocalDateTime.now());
                order.setOrderStatus(OrderingConstants.ORDER_STATUS_VALIDATED);

                order.setAddressStreet(request.getStreet());
                order.setAddressCity(request.getCity());
                order.setAddressState(request.getState());
                order.setAddressCountry(request.getCountry());

                order.setBuyerId(request.getUserId());
                order.setBuyerEmail(request.getUserEmail());

                // Create order items from request
                List<OrderItem> orderItems = request.getOrderItems().stream()
                                .map(itemDto -> {
                                        OrderItem orderItem = new OrderItem();
                                        orderItem.setOrder(order);
                                        orderItem.setProductId(itemDto.getProductId());
                                        orderItem.setProductName(itemDto.getProductName());
                                        orderItem.setUnitPrice(itemDto.getUnitPrice());
                                        orderItem.setUnits(itemDto.getUnits());
                                        orderItem.setPictureUrl(itemDto.getPictureUrl());
                                        return orderItem;
                                })
                                .collect(Collectors.toList());
                order.setOrderItems(orderItems);
                order.setTotalAmount(request.getTotalAmount());

                Order savedOrder = orderRepository.save(order);
                Long orderId = savedOrder.getOrderId();

                log.info("Order created successfully with ID: {} for user: {}", orderId, request.getUserId());
                return orderId;
        }

        @Override
        @Transactional
        public boolean cancelOrder(Long orderId, String reason) {
                log.info("Cancelling order: {} with reason: {}", orderId, reason);

                Order order = orderRepository.findByOrderId(orderId)
                                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId.toString()));

                // Check if order can be cancelled
                if (!canCancelOrder(order.getOrderStatus())) {
                        throw new InvalidOrderStatusException(order.getOrderStatus(), "cancel");
                }

                order.setOrderStatus(OrderingConstants.ORDER_STATUS_CANCELLED);
                orderRepository.save(order);

                List<OrderStockItem> stockItems = order.getOrderItems().stream()
                                .map(item -> new OrderStockItem(item.getProductId(), item.getProductName(),
                                                item.getUnits(),
                                                item.getPictureUrl()))
                                .collect(Collectors.toList());

                // Publish cancellation event
                eventBus.publish(new OrderStatusChangedToCancelledIntegrationEvent(
                                order.getOrderId(),
                                order.getBuyerId(),
                                reason,
                                stockItems));

                // Publish basket cleared event to clear user's basket when order is cancelled
                BasketClearedIntegrationEvent basketClearedEvent = new BasketClearedIntegrationEvent(
                                order.getBuyerId());
                eventBus.publish(basketClearedEvent);
                log.info("✅ BasketClearedIntegrationEvent published for buyerId: {} due to order cancellation",
                                order.getBuyerId());

                publishOrderCancelledForEmailEvent(order, reason);

                log.info("Order cancelled successfully: {}", orderId);
                return true;
        }

        @Override
        @Transactional
        public void createOrderFromCheckoutV2(UserCheckoutAcceptedIntegrationEventV2 event) {
                log.info("Creating order from checkout for user: {}", event.getUserId());

                Order order = new Order();
                Long orderId = event.getOrderId() != null ? event.getOrderId() : generateOrderId();
                order.setOrderId(orderId);
                order.setOrderDate(LocalDateTime.now());
                order.setOrderStatus(OrderingConstants.ORDER_STATUS_VALIDATED);

                order.setAddressStreet(event.getStreet());
                order.setAddressCity(event.getCity());
                order.setAddressState(event.getState());
                order.setAddressCountry(event.getCountry());

                order.setBuyerId(event.getUserId());
                order.setBuyerEmail(event.getUserEmail());

                // Create order items from basket items
                List<OrderItem> orderItems = event.getBasket().getItems().stream()
                                .map(basketItem -> {
                                        OrderItem orderItem = new OrderItem();
                                        orderItem.setOrder(order);
                                        orderItem.setProductId(basketItem.getProductId());
                                        orderItem.setProductName(basketItem.getProductName());
                                        orderItem.setUnitPrice(basketItem.getUnitPrice());
                                        orderItem.setUnits(basketItem.getUnits());
                                        orderItem.setPictureUrl(basketItem.getPictureUrl());
                                        return orderItem;
                                })
                                .collect(Collectors.toList());
                order.setOrderItems(orderItems);
                order.setTotalAmount(event.getTotalAmount());
                orderRepository.save(order);
        }

        @Override
        @Transactional
        public void updateOrderStatusToPaidV2(Long orderId) {
                log.info("Updating order status to Paid: {}", orderId);

                Order order = orderRepository.findByOrderId(orderId)
                                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId.toString()));

                order.setOrderStatus(OrderingConstants.ORDER_STATUS_PAID);
                orderRepository.save(order);

                List<OrderStockItem> stockItems = order.getOrderItems().stream()
                                .map(orderItem -> new OrderStockItem(orderItem.getProductId(),
                                                orderItem.getProductName(),
                                                orderItem.getUnits(), orderItem.getPictureUrl()))
                                .collect(Collectors.toList());

                // Publish paid event
                OrderStatusChangedToPaidIntegrationEventV2 event = new OrderStatusChangedToPaidIntegrationEventV2(
                                orderId,
                                order.getBuyerId(), stockItems);
                eventBus.publish(event);

                log.info("✅ Publishing OrderStatusChangedToPaidIntegrationEventV2 for buyerId: {}", order.getBuyerId());

                // Publish basket cleared event to clear user's basket
                BasketClearedIntegrationEvent basketClearedEvent = new BasketClearedIntegrationEvent(
                                order.getBuyerId());
                eventBus.publish(basketClearedEvent);
                log.info("✅ BasketClearedIntegrationEvent published for buyerId: {}", order.getBuyerId());

        }

        private boolean canCancelOrder(String orderStatus) {
                return OrderingConstants.ORDER_STATUS_SUBMITTED.equals(orderStatus) ||
                                OrderingConstants.ORDER_STATUS_AWAITING_STOCK_VALIDATION.equals(orderStatus) ||
                                OrderingConstants.ORDER_STATUS_VALIDATED.equals(orderStatus);
        }

        private void publishOrderCancelledForEmailEvent(Order order, String reason) {
                List<OrderCancelledForEmailEvent.OrderItemInfo> orderItems = order.getOrderItems().stream()
                                .map(item -> new OrderCancelledForEmailEvent.OrderItemInfo(
                                                item.getProductName(),
                                                item.getUnits(),
                                                item.getUnitPrice().doubleValue(),
                                                item.getPictureUrl()))
                                .collect(Collectors.toList());

                OrderCancelledForEmailEvent event = new OrderCancelledForEmailEvent(
                                order.getOrderId(),
                                order.getBuyerEmail(),
                                order.getBuyerName(),
                                reason,
                                order.getTotalAmount().doubleValue(),
                                orderItems);

                eventBus.publish(event);
                log.info("✅ OrderCancelledForEmailEvent published for OrderId: {}", order.getOrderId());
        }
}
