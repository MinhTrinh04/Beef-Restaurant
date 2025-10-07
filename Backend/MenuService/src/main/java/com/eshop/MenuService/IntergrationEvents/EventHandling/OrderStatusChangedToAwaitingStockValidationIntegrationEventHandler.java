package com.eshop.MenuService.IntergrationEvents.EventHandling;

import com.eshop.MenuService.IntergrationEvents.Events.OrderStatusChangedToAwaitingStockValidationIntegrationEvent;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderStatusChangedToAwaitingStockValidationIntegrationEventHandler implements IIntegrationEventHandler<OrderStatusChangedToAwaitingStockValidationIntegrationEvent> {



    @Override
    public void handle(OrderStatusChangedToAwaitingStockValidationIntegrationEvent event) {
        log.info("OrderStatusChangedToAwaitingStockValidationIntegrationEvent received for OrderId: {}", event.getOrderId());
    }
}
