package com.eshop.basketservice.Controller;


import com.eshop.basketservice.Integrationevents.Events.OrderStatusChangedToSubmittedIntegrationEvent;
import com.eshop.basketservice.Integrationevents.Events.UserCheckoutAcceptedIntegrationEvent;
import com.eshop.basketservice.Integrationevents.eventhandling.OrderStatusChangedToSubmittedIntegrationEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.eshop.basketservice.Constants.BasketConstants.QUEUE_NAME;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = QUEUE_NAME)
public class IntegrationEventController {
    private final OrderStatusChangedToSubmittedIntegrationEventHandler submittedEventHandler;

    @RabbitHandler
    public void handleIntegrationEvent(OrderStatusChangedToSubmittedIntegrationEvent event) {
        submittedEventHandler.handle(event);
    }

}


