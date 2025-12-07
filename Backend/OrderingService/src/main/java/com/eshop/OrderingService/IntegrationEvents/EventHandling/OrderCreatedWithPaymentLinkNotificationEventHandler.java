package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.IntegrationEvents.Events.OrderCreatedForEmailEvent;
import com.eshop.OrderingService.IntegrationEvents.Events.OrderCreatedWithPaymentLinkNotificationEvent;
import com.eshop.OrderingService.Model.Order;
import com.eshop.OrderingService.Repository.OrderRepository;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OrderCreatedWithPaymentLinkNotificationEventHandler
                implements IIntegrationEventHandler<OrderCreatedWithPaymentLinkNotificationEvent> {

        private final OrderRepository orderRepository;
        private final IEventBus eventBus;

        @Override
        @Transactional(readOnly = true)
        public void handle(OrderCreatedWithPaymentLinkNotificationEvent event) {
                log.info("📧 OrderCreatedWithPaymentLinkNotificationEvent received for OrderId: {}",
                                event.getOrderId());

                try {
                        // Fetch order from database with lazy-loaded orderItems in active transaction
                        Order order = orderRepository.findByOrderId(event.getOrderId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Order not found: " + event.getOrderId()));

                        // Build OrderItemInfo list from OrderItems
                        List<OrderCreatedForEmailEvent.OrderItemInfo> orderItems = order.getOrderItems().stream()
                                        .map(item -> new OrderCreatedForEmailEvent.OrderItemInfo(
                                                        item.getProductName(),
                                                        item.getUnits(),
                                                        item.getUnitPrice().doubleValue(),
                                                        item.getPictureUrl()))
                                        .collect(Collectors.toList());

                        // Create enriched email event with full order details and payment link
                        OrderCreatedForEmailEvent emailEvent = new OrderCreatedForEmailEvent(
                                        event.getOrderId(),
                                        order.getBuyerEmail(),
                                        order.getBuyerName(),
                                        order.getTotalAmount().doubleValue(),
                                        event.getPaymentUrl(),
                                        orderItems);

                        // Publish enriched event to UserService
                        eventBus.publish(emailEvent);
                        log.info("✅ OrderCreatedForEmailEvent published for OrderId: {}", event.getOrderId());

                } catch (Exception e) {
                        log.error("❌ Failed to handle OrderCreatedWithPaymentLinkNotificationEvent for OrderId: {}. Error: {}",
                                        event.getOrderId(), e.getMessage(), e);
                }
        }
}
