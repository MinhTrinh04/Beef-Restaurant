package com.eshop.PaymentService.IntegrationEvent.EventHandling;

import com.eshop.PaymentService.IntegrationEvent.Events.OrderStatusChangedToValidatedIntegrationEvent;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderStatusChangedToValidatedIntegrationEventHandler implements IIntegrationEventHandler<OrderStatusChangedToValidatedIntegrationEvent> {


    @Override
    public void handle(OrderStatusChangedToValidatedIntegrationEvent event) {
        log.info("⏳ OrderStatusChangedToValidatedIntegrationEvent received for OrderId: {}", event.getOrderId());




    }

}
