package com.eshop.MenuService.Controller;

import com.eshop.MenuService.IntergrationEvents.EventHandling.OrderStatusChangedToAwaitingStockValidationIntegrationEventHandler;
import com.eshop.MenuService.IntergrationEvents.EventHandling.OrderStatusChangedToPaidIntegrationEventHandler;
import com.eshop.MenuService.IntergrationEvents.Events.OrderStatusChangedToAwaitingStockValidationIntegrationEvent;
import com.eshop.MenuService.IntergrationEvents.Events.OrderStatusChangedToPaidIntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IntergrationEventController {
    private final OrderStatusChangedToPaidIntegrationEventHandler paidEventHandler;
    private final OrderStatusChangedToAwaitingStockValidationIntegrationEventHandler awaitingStockEventHandler;

    /**
     * Lắng nghe các sự kiện có routing key là "OrderStatusChangedToPaidIntegrationEvent".
     * Spring AMQP, với MessageConverter đã cấu hình, sẽ tự động deserialize message JSON
     * thành đối tượng OrderStatusChangedToPaidIntegrationEvent.
     */
    @RabbitListener(queues = "#{menuServiceQueue.name}", bindings = @QueueBinding(
            value = @Queue(value = "menu-service-queue", durable = "true"),
            exchange = @Exchange(value = "eshop_event_bus", type = "topic"),
            key = "OrderStatusChangedToPaidIntegrationEvent"
    ))
    public void handleOrderStatusChangedToPaid(OrderStatusChangedToPaidIntegrationEvent event) {
        paidEventHandler.handle(event);
    }

    /**
     * Lắng nghe các sự kiện có routing key là "OrderStatusChangedToAwaitingStockValidationIntegrationEvent".
     */
    @RabbitListener(queues = "#{menuServiceQueue.name}", bindings = @QueueBinding(
            value = @Queue(value = "menu-service-queue", durable = "true"),
            exchange = @Exchange(value = "eshop_event_bus", type = "topic"),
            key = "OrderStatusChangedToAwaitingStockValidationIntegrationEvent"
    ))
    public void handleOrderStatusChangedToAwaitingStockValidation(OrderStatusChangedToAwaitingStockValidationIntegrationEvent event) {
        awaitingStockEventHandler.handle(event);
    }

}
