package com.eshop.UserService.IntegrationEvents.EventHandling;

import com.eshop.UserService.Service.EmailService;
import com.eshop.UserService.IntegrationEvents.Events.OrderCreatedForEmailEvent;
import com.eshop.UserService.IntegrationEvents.Events.OrderPaidForEmailEvent;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles OrderPaidForEmailEvent from OrderingService
 * Sends payment success email notification to user
 */
@Service
@Slf4j
@AllArgsConstructor
public class OrderPaidForEmailEventHandler implements IIntegrationEventHandler<OrderPaidForEmailEvent> {

        private final EmailService emailService;

        @Override
        public void handle(OrderPaidForEmailEvent event) {
                log.info("📧 OrderPaidForEmailEvent received for OrderId: {}", event.getOrderId());

                try {
                        // Convert OrderPaidForEmailEvent items to EmailService item format
                        List<OrderCreatedForEmailEvent.OrderItemInfo> orderItems = event.getOrderItems().stream()
                                        .map(item -> new OrderCreatedForEmailEvent.OrderItemInfo(
                                                        item.getProductName(),
                                                        item.getUnits(),
                                                        item.getUnitPrice(),
                                                        item.getPictureUrl()))
                                        .collect(Collectors.toList());

                        // Send payment success email
                        emailService.sendOrderPaidEmail(
                                        event.getUserEmail(),
                                        event.getUserName(),
                                        event.getOrderId(),
                                        event.getTotalAmount(),
                                        orderItems);

                        log.info("✅ Order paid email sent for OrderId: {}", event.getOrderId());

                } catch (Exception e) {
                        log.error("❌ Failed to send order paid email for OrderId: {}. Error: {}", event.getOrderId(),
                                        e.getMessage(), e);
                }
        }
}
