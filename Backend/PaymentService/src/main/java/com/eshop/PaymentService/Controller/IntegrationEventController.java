package com.eshop.PaymentService.Controller;

import com.eshop.PaymentService.IntegrationEvent.EventHandling.OrderStatusChangedToValidatedIntegrationEventHandler;
import com.eshop.PaymentService.IntegrationEvent.Events.OrderStatusChangedToValidatedIntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = "payment-service-queue")
public class IntegrationEventController {
    private final OrderStatusChangedToValidatedIntegrationEventHandler validatedIntegrationEventHandler;

    @RabbitHandler
    public void handleOrderStatusChangedToValidatedIntegrationEvent(OrderStatusChangedToValidatedIntegrationEvent event) {
        validatedIntegrationEventHandler.handle(event);
    }


}
