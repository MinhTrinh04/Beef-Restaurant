package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.IntegrationEvents.Events.OrderStockConfirmedIntegrationEvent;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderStockConfirmedIntegrationEventHandler
        implements IIntegrationEventHandler<OrderStockConfirmedIntegrationEvent> {

    private final IOrderingService orderingService;

    @Override
    public void handle(OrderStockConfirmedIntegrationEvent event) {
        log.info("✅ OrderStockConfirmedIntegrationEvent received for OrderId: {}", event.getOrderId());

        try {
            // Update order status to validated
            orderingService.updateOrderStatusToValidated(event.getOrderId());
            log.info("✅ Order status updated to Validated for OrderId: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("❌ Failed to update order status for OrderId: {}. Error: {}", event.getOrderId(), e.getMessage(),
                    e);
        }
    }
}
