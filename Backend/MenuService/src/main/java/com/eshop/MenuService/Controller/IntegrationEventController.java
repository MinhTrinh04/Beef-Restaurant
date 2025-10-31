package com.eshop.MenuService.Controller;

import com.eshop.MenuService.IntegrationEvents.EventHandling.OrderStatusChangedToAwaitingStockValidationIntegrationEventHandler;
import com.eshop.MenuService.IntegrationEvents.EventHandling.OrderStatusChangedToAwaitingStockValidationIntegrationEventV2Handler;
import com.eshop.MenuService.IntegrationEvents.EventHandling.OrderStatusChangedToPaidIntegrationEventHandler;
import com.eshop.MenuService.IntegrationEvents.EventHandling.OrderStatusChangedToPaidIntegrationEventV2Handler;
import com.eshop.MenuService.IntegrationEvents.Events.OrderStatusChangedToAwaitingStockValidationIntegrationEvent;
import com.eshop.MenuService.IntegrationEvents.Events.OrderStatusChangedToAwaitingStockValidationIntegrationEventV2;
import com.eshop.MenuService.IntegrationEvents.Events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshop.MenuService.IntegrationEvents.Events.OrderStatusChangedToPaidIntegrationEventV2;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.eshop.MenuService.Constants.MenuConstants.QUEUE_NAME;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = QUEUE_NAME)
public class IntegrationEventController {
    private final OrderStatusChangedToPaidIntegrationEventHandler paidEventHandler;
    private final OrderStatusChangedToPaidIntegrationEventV2Handler paidEventV2Handler;
    private final OrderStatusChangedToAwaitingStockValidationIntegrationEventHandler awaitingStockEventHandler;
    private final OrderStatusChangedToAwaitingStockValidationIntegrationEventV2Handler awaitingStockEventV2Handler;

    @RabbitHandler
    public void handleOrderStatusChangedToPaid(OrderStatusChangedToPaidIntegrationEvent event) {
        paidEventHandler.handle(event);
    }

    @RabbitHandler
    public void handleOrderStatusChangedToPaidV2(OrderStatusChangedToPaidIntegrationEventV2 event) {
        paidEventV2Handler.handle(event);
    }

    @RabbitHandler
    public void handleOrderStatusChangedToAwaitingStockValidation(OrderStatusChangedToAwaitingStockValidationIntegrationEvent event) {
        awaitingStockEventHandler.handle(event);
    }

    @RabbitHandler
    public void handleOrderStatusChangedToAwaitingStockValidationV2(OrderStatusChangedToAwaitingStockValidationIntegrationEventV2 event) {
        awaitingStockEventV2Handler.handle(event);
    }

}
