package com.eshop.MenuService.Controller;

import com.eshop.MenuService.IntegrationEvents.EventHandling.OrderStatusChangedToAwaitingStockValidationIntegrationEventHandler;
import com.eshop.MenuService.IntegrationEvents.EventHandling.OrderStatusChangedToPaidIntegrationEventHandler;
import com.eshop.MenuService.IntegrationEvents.Events.OrderStatusChangedToAwaitingStockValidationIntegrationEvent;
import com.eshop.MenuService.IntegrationEvents.Events.OrderStatusChangedToPaidIntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = "menu-service-queue")
public class IntegrationEventController {
    private final OrderStatusChangedToPaidIntegrationEventHandler paidEventHandler;
    private final OrderStatusChangedToAwaitingStockValidationIntegrationEventHandler awaitingStockEventHandler;

    /**
     * Lắng nghe các sự kiện có routing key là "OrderStatusChangedToPaidIntegrationEvent".
     */
    @RabbitHandler
    public void handleOrderStatusChangedToPaid(OrderStatusChangedToPaidIntegrationEvent event) {
        paidEventHandler.handle(event);
    }

    /**
     * Lắng nghe các sự kiện có routing key là "OrderStatusChangedToAwaitingStockValidationIntegrationEvent".
     */
    @RabbitHandler
    public void handleOrderStatusChangedToAwaitingStockValidation(OrderStatusChangedToAwaitingStockValidationIntegrationEvent event) {
        awaitingStockEventHandler.handle(event);
    }

}
