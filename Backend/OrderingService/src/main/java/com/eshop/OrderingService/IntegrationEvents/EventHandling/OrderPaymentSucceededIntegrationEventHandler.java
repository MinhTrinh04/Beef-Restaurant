package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.IntegrationEvents.Events.OrderPaymentSucceededIntegrationEvent;
import com.eshop.OrderingService.IntegrationEvents.Events.OrderPaidForEmailEvent;
import com.eshop.OrderingService.Model.Order;
import com.eshop.OrderingService.Repository.OrderRepository;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles OrderPaymentSucceededIntegrationEvent from PaymentService
 * 1. Updates order status to Paid
 * 2. Publishes email event to UserService for payment confirmation
 */
@Service
@Slf4j
@AllArgsConstructor
public class OrderPaymentSucceededIntegrationEventHandler
        implements IIntegrationEventHandler<OrderPaymentSucceededIntegrationEvent> {

    private final IOrderingService orderingService;
    private final OrderRepository orderRepository;
    private final IEventBus eventBus;

    @Override
    @Transactional
    public void handle(OrderPaymentSucceededIntegrationEvent event) {
        log.info("💰 OrderPaymentSucceededIntegrationEvent received for OrderId: {}", event.getOrderId());

        try {
            // Step 1: Update order status to paid
            orderingService.updateOrderStatusToPaidV2(event.getOrderId());
            log.info("✅ Order status updated to Paid for OrderId: {}", event.getOrderId());

            // Step 2: Fetch order details for email event
            Order order = orderRepository.findByOrderId(event.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));

            // Build OrderItemInfo list from OrderItems
            List<OrderPaidForEmailEvent.OrderItemInfo> orderItems = order.getOrderItems().stream()
                    .map(item -> new OrderPaidForEmailEvent.OrderItemInfo(
                            item.getProductName(),
                            item.getUnits(),
                            item.getUnitPrice().doubleValue(),
                            item.getPictureUrl()))
                    .collect(Collectors.toList());

            // Create and publish email event to UserService
            OrderPaidForEmailEvent emailEvent = new OrderPaidForEmailEvent(
                    event.getOrderId(),
                    order.getBuyerEmail(),
                    order.getBuyerName(),
                    order.getTotalAmount().doubleValue(),
                    orderItems);

            eventBus.publish(emailEvent);
            log.info("✅ OrderPaidForEmailEvent published for OrderId: {}", event.getOrderId());

        } catch (Exception e) {
            log.error("❌ Failed to handle OrderPaymentSucceededIntegrationEvent for OrderId: {}. Error: {}",
                    event.getOrderId(), e.getMessage(), e);
        }
    }
}
