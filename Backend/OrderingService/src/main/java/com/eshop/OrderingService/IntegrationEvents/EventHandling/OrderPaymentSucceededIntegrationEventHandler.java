package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.IntegrationEvents.Events.OrderPaymentSucceededIntegrationEvent;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderPaymentSucceededIntegrationEventHandler
        implements IIntegrationEventHandler<OrderPaymentSucceededIntegrationEvent> {

    private final IOrderingService orderingService;

    @Override
    public void handle(OrderPaymentSucceededIntegrationEvent event) {
        log.info("💰 OrderPaymentSucceededIntegrationEvent received for OrderId: {}", event.getOrderId());

        try {
            // Update order status to paid
            orderingService.updateOrderStatusToPaidV2(event.getOrderId());
            log.info("✅ Order status updated to Paid for OrderId: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("❌ Failed to update order status to Paid for OrderId: {}. Error: {}", event.getOrderId(),
                    e.getMessage(), e);
        }
    }
}
