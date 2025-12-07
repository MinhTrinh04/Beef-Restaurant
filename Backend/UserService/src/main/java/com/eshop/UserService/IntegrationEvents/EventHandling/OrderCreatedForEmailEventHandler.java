package com.eshop.UserService.IntegrationEvents.EventHandling;

import com.eshop.UserService.IntegrationEvents.Events.OrderCreatedForEmailEvent;
import com.eshop.UserService.Service.EmailService;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@AllArgsConstructor
public class OrderCreatedForEmailEventHandler implements IIntegrationEventHandler<OrderCreatedForEmailEvent> {

    private final EmailService emailService;

    @Override
    public void handle(OrderCreatedForEmailEvent event) {
        log.info("📧 OrderCreatedForEmailEvent received for OrderId: {}", event.getOrderId());

        try {
            emailService.sendOrderCreatedWithPaymentLinkEmail(
                    event.getUserEmail(),
                    event.getUserName(),
                    event.getOrderId(),
                    event.getTotalAmount(),
                    event.getPaymentUrl(),
                    event.getOrderItems());
            log.info("✅ Order confirmation email sent for OrderId: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("❌ Failed to send order creation email for OrderId: {}. Error: {}",
                    event.getOrderId(), e.getMessage(), e);
        }
    }
}
