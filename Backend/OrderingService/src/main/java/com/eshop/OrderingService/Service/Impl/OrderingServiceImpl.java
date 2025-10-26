package com.eshop.OrderingService.Service.Impl;

import com.eshop.OrderingService.Constants.OrderingConstants;
import com.eshop.OrderingService.DTO.CreateOrderRequestDto;
import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.DTO.OrderItemDto;
import com.eshop.OrderingService.DTO.OrderItemRequestDto;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OrderingServiceImpl implements IOrderingService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IEventBus eventBus;

//    @Override
//    @Transactional
//    public OrderDto createOrder(CreateOrderRequestDto request) {
//        log.info("Creating new order for user: {}", request.getUserId());
//
//        Order order = new Order();
//        order.setOrderId(UUID.randomUUID().toString());
//        order.setUserId(request.getUserId());
//        order.setOrderDate(LocalDateTime.now());
//        order.setOrderStatus(OrderingConstants.ORDER_STATUS_SUBMITTED);
//        order.setDescription(request.getDescription());
//
//        // Set address
//        order.setAddressStreet(request.getAddressStreet());
//        order.setAddressCity(request.getAddressCity());
//        order.setAddressState(request.getAddressState());
//        order.setAddressCountry(request.getAddressCountry());
//        order.setAddressZipCode(request.getAddressZipCode());
//
//        // Set payment info
//        order.setCardNumber(request.getCardNumber());
//        order.setCardHolderName(request.getCardHolderName());
//        order.setCardSecurityNumber(request.getCardSecurityNumber());
//        order.setCardTypeId(request.getCardTypeId());
//
//        // Set buyer info
//        order.setBuyerName(request.getBuyerName());
//        order.setBuyerEmail(request.getBuyerEmail());
//
//        // Parse card expiration
//        if (request.getCardExpiration() != null) {
//            try {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
//                order.setCardExpiration(LocalDateTime.parse("01/" + request.getCardExpiration(), formatter));
//            } catch (Exception e) {
//                log.warn("Failed to parse card expiration: {}", request.getCardExpiration());
//            }
//        }
//
//        // Calculate total amount
//        BigDecimal totalAmount = request.getOrderItems().stream()
//                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getUnits())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        order.setTotalAmount(totalAmount);
//
//        // Save order
//        Order savedOrder = orderRepository.save(order);
//
//        // Create order items
//        List<OrderItem> orderItems = request.getOrderItems().stream()
//                .map(itemRequest -> {
//                    OrderItem orderItem = new OrderItem();
//                    orderItem.setOrder(savedOrder);
//                    orderItem.setProductId(itemRequest.getProductId());
//                    orderItem.setProductName(itemRequest.getProductName());
//                    orderItem.setUnitPrice(itemRequest.getUnitPrice());
//                    orderItem.setUnits(itemRequest.getUnits());
//                    orderItem.setPictureUrl(itemRequest.getPictureUrl());
//                    return orderItem;
//                })
//                .collect(Collectors.toList());
//
//        savedOrder.setOrderItems(orderItems);
//        orderRepository.save(savedOrder);
//
//        // Publish event for stock validation
//        processOrderSubmission(savedOrder.getOrderId());
//
//        log.info("Order created successfully with ID: {}", savedOrder.getOrderId());
//        return orderMapper.toDto(savedOrder);
//    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order", "id", id.toString()));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId));
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
    public boolean cancelOrder(String orderId, String reason) {
        log.info("Cancelling order: {} with reason: {}", orderId, reason);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId));

        // Check if order can be cancelled
        if (!canCancelOrder(order.getOrderStatus())) {
            throw new InvalidOrderStatusException(order.getOrderStatus(), "cancel");
        }

        order.setOrderStatus(OrderingConstants.ORDER_STATUS_CANCELLED);
        orderRepository.save(order);

        // Publish cancellation event
        OrderStatusChangedToCancelledIntegrationEvent event = new OrderStatusChangedToCancelledIntegrationEvent(orderId,
                order.getBuyerId(), reason);
        eventBus.publish(event);

        log.info("Order cancelled successfully: {}", orderId);
        return true;
    }

    @Override
    @Transactional
    public boolean shipOrder(String orderId) {
        log.info("Shipping order: {}", orderId);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId));

        // Check if order can be shipped
        if (!OrderingConstants.ORDER_STATUS_PAID.equals(order.getOrderStatus())) {
            throw new InvalidOrderStatusException(order.getOrderStatus(), "ship");
        }

        order.setOrderStatus(OrderingConstants.ORDER_STATUS_SHIPPED);
        orderRepository.save(order);

        // Publish shipped event
        OrderStatusChangedToShippedIntegrationEvent event = new OrderStatusChangedToShippedIntegrationEvent(orderId,
                order.getBuyerId());
        eventBus.publish(event);

        log.info("Order shipped successfully: {}", orderId);
        return true;
    }

    @Override
    @Transactional
    public void createOrderFromCheckout(UserCheckoutAcceptedIntegrationEvent event) {
        log.info("Creating order from checkout for user: {}", event.getUserId());

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderingConstants.ORDER_STATUS_SUBMITTED);

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

        // Calculate total amount
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getUnits())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Publish submitted event
        OrderStatusChangedToSubmittedIntegrationEvent submittedEvent = new OrderStatusChangedToSubmittedIntegrationEvent(
                savedOrder.getOrderId(), savedOrder.getBuyerId());
        eventBus.publish(submittedEvent);
        log.info("✅ OrderStatusChangedToSubmittedIntegrationEvent published for OrderId: {}", savedOrder.getOrderId());

        // Start processing
        processOrderSubmission(savedOrder.getOrderId());
    }

    @Override
    @Transactional
    public void updateOrderStatusToValidated(String orderId) {
        log.info("Updating order status to Validated: {}", orderId);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId));

        order.setOrderStatus(OrderingConstants.ORDER_STATUS_VALIDATED);
        orderRepository.save(order);

        // Publish validated event
        OrderStatusChangedToValidatedIntegrationEvent event = new OrderStatusChangedToValidatedIntegrationEvent(orderId,
                order.getBuyerId());
        eventBus.publish(event);

        log.info("Order status updated to Validated: {}", orderId);
    }

    @Override
    @Transactional
    public void updateOrderStatusToPaid(String orderId) {
        log.info("Updating order status to Paid: {}", orderId);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId));

        order.setOrderStatus(OrderingConstants.ORDER_STATUS_PAID);
        orderRepository.save(order);

        // Publish paid event
        OrderStatusChangedToPaidIntegrationEvent event = new OrderStatusChangedToPaidIntegrationEvent(orderId,
                order.getBuyerId());
        eventBus.publish(event);

        log.info("Order status updated to Paid: {}", orderId);
    }

    @Override
    public void processOrderSubmission(String orderId) {
        log.info("Processing order submission: {}", orderId);
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId));

        // Convert order items to stock items for validation
        List<OrderStockItem> orderStockItems = order.getOrderItems().stream()
                .map(item -> new OrderStockItem(
                        item.getProductId(),
                        item.getProductName(),
                        item.getUnits(),
                        item.getPictureUrl()))
                .collect(Collectors.toList());

        // Publish stock validation event
        OrderStatusChangedToAwaitingStockValidationIntegrationEvent event = new OrderStatusChangedToAwaitingStockValidationIntegrationEvent(
                orderId, order.getBuyerId(), orderStockItems);
        eventBus.publish(event);

        log.info("Stock validation event published for order: {}", orderId);
    }

    private boolean canCancelOrder(String orderStatus) {
        return OrderingConstants.ORDER_STATUS_SUBMITTED.equals(orderStatus) ||
                OrderingConstants.ORDER_STATUS_AWAITING_STOCK_VALIDATION.equals(orderStatus) ||
                OrderingConstants.ORDER_STATUS_VALIDATED.equals(orderStatus);
    }
}
