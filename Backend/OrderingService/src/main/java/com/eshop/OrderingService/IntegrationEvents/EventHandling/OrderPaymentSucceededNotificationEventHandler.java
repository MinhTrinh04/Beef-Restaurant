package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.IntegrationEvents.Events.OrderPaymentSucceededNotificationEvent;
import com.eshop.OrderingService.IntegrationEvents.Events.OrderPaidForEmailEvent;
import com.eshop.OrderingService.Model.Order;
import com.eshop.OrderingService.Repository.OrderRepository;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles notification from PaymentService when payment succeeds
 * Fetches order details from database and publishes enriched email event to
 * UserService
 */
@Service
@Slf4j
@AllArgsConstructor
public class OrderPaymentSucceededNotificationEventHandler
        implements IIntegrationEventHandler<OrderPaymentSucceededNotificationEvent> {

    private final OrderRepository orderRepository;
    private final IEventBus eventBus;

    @Override
    public void handle(OrderPaymentSucceededNotificationEvent event) {
        log.info("💳 OrderPaymentSucceededNotificationEvent received for OrderId: {}", event.getOrderId());

        try {
            // Fetch order from database
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

            // Create enriched email event with full order details
            OrderPaidForEmailEvent emailEvent = new OrderPaidForEmailEvent(
                    event.getOrderId(),
                    order.getBuyerEmail(),
                    order.getBuyerName(),
                    order.getTotalAmount().doubleValue(),
                    orderItems);

            // Publish enriched event to UserService
            eventBus.publish(emailEvent);
            log.info("✅ OrderPaidForEmailEvent published for OrderId: {}", event.getOrderId());

        } catch (Exception e) {
            log.error("❌ Failed to handle OrderPaymentSucceededNotificationEvent for OrderId: {}. Error: {}",
                    event.getOrderId(), e.getMessage(), e);
        }
    }
}
