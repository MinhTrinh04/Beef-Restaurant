package com.eshop.MenuService.IntergrationEvents.EventHandling;

import com.eshop.MenuService.IntergrationEvents.Events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderStatusChangedToPaidIntegrationEventHandler implements IIntegrationEventHandler<OrderStatusChangedToPaidIntegrationEvent> {

    @Override
    public void handle(OrderStatusChangedToPaidIntegrationEvent event) {
        log.info("OrderStatusChangedToPaidIntegrationEvent received for OrderId: {}", event.getOrderId());
    }

}
