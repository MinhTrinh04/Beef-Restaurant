package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.IntegrationEvents.Events.OrderPaymentCancelledIntegrationEvent;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderPaymentCancelledIntegrationEventHandler
        implements IIntegrationEventHandler<OrderPaymentCancelledIntegrationEvent> {

    private final IOrderingService orderingService;

    @Override
    public void handle(OrderPaymentCancelledIntegrationEvent event) {
        log.info("🚫 OrderPaymentCancelledIntegrationEvent received for OrderId: {}", event.getOrderId());

        try {
            // Cancel order due to payment cancellation
            orderingService.cancelOrder(event.getOrderId(), "Payment cancelled: " + event.getReason());
            log.info("✅ Order cancelled due to payment cancellation for OrderId: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("❌ Failed to cancel order for OrderId: {}. Error: {}", event.getOrderId(), e.getMessage(), e);
        }
    }
}
