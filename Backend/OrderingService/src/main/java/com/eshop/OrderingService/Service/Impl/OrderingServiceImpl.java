package com.eshop.OrderingService.Service.Impl;

import com.eshop.OrderingService.Constants.OrderingConstants;
import com.eshop.OrderingService.DTO.*;
import com.eshop.OrderingService.Exception.InvalidOrderStatusException;
import com.eshop.OrderingService.Exception.OrderNotFoundException;
import com.eshop.OrderingService.IntegrationEvents.Events.*;
import com.eshop.OrderingService.Mapper.OrderMapper;
import com.eshop.OrderingService.Model.Order;
import com.eshop.OrderingService.Model.OrderItem;
import com.eshop.OrderingService.Repository.OrderRepository;
import com.eshop.OrderingService.Service.Client.PaymentServiceClient;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final PaymentServiceClient paymentServiceClient;

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order", "id", id.toString()));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto getOrderByOrderId(UUID orderId) {
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
    public boolean cancelOrder(UUID orderId, String reason) {
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
                .map(item -> new OrderStockItem(item.getProductId(), item.getProductName(), item.getUnits(), item.getPictureUrl()))
                .collect(Collectors.toList());

        // Publish cancellation event
        eventBus.publish(new OrderStatusChangedToCancelledIntegrationEvent(
                order.getOrderId(),
                order.getBuyerId(),
                "Order cancelled",
                stockItems
        ));

        log.info("Order cancelled successfully: {}", orderId);
        return true;
    }

    @Override
    @Transactional
    public boolean shipOrder(UUID orderId) {
        log.info("Shipping order: {}", orderId);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId.toString()));

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
        log.info("Creating order from checkoutV2 for user: {}", event.getUserId());

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
        order.setOrderItems(orderItems);

        // Calculate total amount - Xem xét lại cần lưu khi nào
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getUnits())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Publish submitted event
        OrderStatusChangedToSubmittedIntegrationEvent submittedEvent = new OrderStatusChangedToSubmittedIntegrationEvent(
                savedOrder.getOrderId(), savedOrder.getBuyerId(),savedOrder.getBuyerEmail());
        eventBus.publish(submittedEvent);
        log.info("✅ OrderStatusChangedToSubmittedIntegrationEvent published for OrderId: {}", savedOrder.getOrderId());

        // Start processing
        processOrderSubmission(savedOrder.getOrderId());
    }

    @Override
    @Transactional
    public void createOrderFromCheckoutV2(UserCheckoutAcceptedIntegrationEventV2 event) {
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
        order.setOrderItems(orderItems);

        // Calculate total amount - Xem xét lại cần lưu khi nào
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getUnits())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Start processing
        processOrderSubmissionV2(savedOrder.getOrderId());
    }

    @Override
    @Transactional
    public void updateOrderStatusToValidated(UUID orderId) {
        log.info("Updating order status to Validated: {}", orderId);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId.toString()));

        order.setOrderStatus(OrderingConstants.ORDER_STATUS_VALIDATED);

        try {
            CreatePaymentUrlRequestDto paymentRequest = new CreatePaymentUrlRequestDto();
            paymentRequest.setOrderId(order.getOrderId());
            paymentRequest.setAmount(order.getTotalAmount());

            // (Bạn nên cải tiến để BasketCheckout mang theo 2 trường này)
            paymentRequest.setBankCode(null);
            paymentRequest.setLanguage("vn");

            // [SỬA] Gọi Feign client chính xác
            ResponseEntity<PaymentUrlResponseDto> responseEntity = paymentServiceClient.createPaymentUrl(paymentRequest);

            PaymentUrlResponseDto responseBody = responseEntity.getBody();

            // [SỬA] Kiểm tra response code từ PaymentService
            if (responseEntity.getStatusCode().is2xxSuccessful() &&
                    responseBody != null &&
                    "00".equals(responseBody.getCode())) {

                order.setPaymentUrl(responseBody.getPaymentUrl());
                log.info("Payment URL received and saved for order: {}", order.getOrderId());
            } else {
                // Ném lỗi nếu PaymentService trả về lỗi
                String errorMessage = (responseBody != null) ? responseBody.getMessage() : "Unknown error from PaymentService";
                throw new Exception("Failed to create payment URL: " + errorMessage);
            }

        } catch (Exception e) {
            log.error("Failed to create payment link: {}", e.getMessage(), e);
            return;
        }
        orderRepository.save(order);


        // Publish validated event luồng cũ
//        OrderStatusChangedToValidatedIntegrationEvent event = new OrderStatusChangedToValidatedIntegrationEvent(orderId,
//                order.getBuyerId());
//        eventBus.publish(event);
//
//        log.info("✅ Publishing OrderStatusChangedToValidatedIntegrationEvent for buyerId: {}", order.getBuyerId());
    }

    @Override
    @Transactional
    public void updateOrderStatusToPaidV2(UUID orderId) {
        log.info("Updating order status to Paid: {}", orderId);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId.toString()));

        order.setOrderStatus(OrderingConstants.ORDER_STATUS_PAID);
        orderRepository.save(order);

        List<OrderStockItem> stockItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderStockItem(orderItem.getProductId(), orderItem.getProductName(), orderItem.getUnits(),  orderItem.getPictureUrl()))
                .collect(Collectors.toList());

        // Publish paid event
        OrderStatusChangedToPaidIntegrationEventV2 event = new OrderStatusChangedToPaidIntegrationEventV2(orderId,
                order.getBuyerId(),stockItems);
        eventBus.publish(event);

        log.info("✅ Publishing OrderStatusChangedToPaidIntegrationEventV2 for buyerId: {}", order.getBuyerId());

        OrderStatusChangedToSubmittedIntegrationEvent submittedEvent = new OrderStatusChangedToSubmittedIntegrationEvent(
                order.getOrderId(), order.getBuyerId(),order.getBuyerEmail());
        eventBus.publish(submittedEvent);
        log.info("✅ OrderStatusChangedToSubmittedIntegrationEventV2 published for OrderId: {}", order.getOrderId());

    }

    @Override
    @Transactional
    public void updateOrderStatusToPaid(UUID orderId) {
        log.info("Updating order status to Paid: {}", orderId);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId.toString()));

        order.setOrderStatus(OrderingConstants.ORDER_STATUS_PAID);
        orderRepository.save(order);

        // Publish paid event
        OrderStatusChangedToPaidIntegrationEvent event = new OrderStatusChangedToPaidIntegrationEvent(orderId,
                order.getBuyerId());
        eventBus.publish(event);

        log.info("✅ Publishing OrderStatusChangedToPaidIntegrationEvent for buyerId: {}", order.getBuyerId());
        //Thêm tạm luồng pub cho luồng mới
//        OrderStatusChangedToSubmittedIntegrationEvent submittedEvent = new OrderStatusChangedToSubmittedIntegrationEvent(
//                order.getOrderId(), order.getBuyerId(),order.getBuyerEmail());
//        eventBus.publish(submittedEvent);
//        log.info("✅ OrderStatusChangedToSubmittedIntegrationEventV2 published for OrderId: {}", order.getOrderId());

    }

    @Override
    public void processOrderSubmission(UUID orderId) {
        log.info("Processing order submission: {}", orderId);
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId.toString()));

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

        log.info("✅ Publishing OrderStatusChangedToAwaitingStockValidationIntegrationEvent for buyerId {}", order.getBuyerId());
    }

    @Override
    public void processOrderSubmissionV2(UUID orderId) {
        log.info("Processing order submission: {}", orderId);
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order", "orderId", orderId.toString()));

        // Convert order items to stock items for validation
        List<OrderStockItem> orderStockItems = order.getOrderItems().stream()
                .map(item -> new OrderStockItem(
                        item.getProductId(),
                        item.getProductName(),
                        item.getUnits(),
                        item.getPictureUrl()))
                .collect(Collectors.toList());

        // Publish stock validation event
        OrderStatusChangedToAwaitingStockValidationIntegrationEventV2 event = new OrderStatusChangedToAwaitingStockValidationIntegrationEventV2(
                orderId, order.getBuyerId(), orderStockItems);

        eventBus.publish(event);

        log.info("✅ Publishing OrderStatusChangedToAwaitingStockValidationIntegrationEventV2 for buyerId {}", order.getBuyerId());
    }

    private boolean canCancelOrder(String orderStatus) {
        return OrderingConstants.ORDER_STATUS_SUBMITTED.equals(orderStatus) ||
                OrderingConstants.ORDER_STATUS_AWAITING_STOCK_VALIDATION.equals(orderStatus) ||
                OrderingConstants.ORDER_STATUS_VALIDATED.equals(orderStatus);
    }
}
