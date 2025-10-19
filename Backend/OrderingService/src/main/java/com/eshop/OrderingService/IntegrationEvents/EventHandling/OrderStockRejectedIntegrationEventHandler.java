package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.IntegrationEvents.Events.OrderStockRejectedIntegrationEvent;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderStockRejectedIntegrationEventHandler
        implements IIntegrationEventHandler<OrderStockRejectedIntegrationEvent> {

    private final IOrderingService orderingService;

    @Override
    public void handle(OrderStockRejectedIntegrationEvent event) {
        log.info("❌ OrderStockRejectedIntegrationEvent received for OrderId: {}", event.getOrderId());

        try {
            // Cancel order due to insufficient stock
            orderingService.cancelOrder(event.getOrderId(), "Insufficient stock");
            log.info("✅ Order cancelled due to insufficient stock for OrderId: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("❌ Failed to cancel order for OrderId: {}. Error: {}", event.getOrderId(), e.getMessage(), e);
        }
    }
}
