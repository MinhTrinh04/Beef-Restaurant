package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.IntegrationEvents.Events.OrderPaymentFailedIntegrationEvent;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderPaymentFailedIntegrationEventHandler
        implements IIntegrationEventHandler<OrderPaymentFailedIntegrationEvent> {

    private final IOrderingService orderingService;

    @Override
    public void handle(OrderPaymentFailedIntegrationEvent event) {
        log.info("💳 OrderPaymentFailedIntegrationEvent received for OrderId: {}", event.getOrderId());

        try {
            // Cancel order due to payment failure
            orderingService.cancelOrder(event.getOrderId(), "Payment failed: " + event.getReason());
            log.info("✅ Order cancelled due to payment failure for OrderId: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("❌ Failed to cancel order for OrderId: {}. Error: {}", event.getOrderId(), e.getMessage(), e);
        }
    }
}
