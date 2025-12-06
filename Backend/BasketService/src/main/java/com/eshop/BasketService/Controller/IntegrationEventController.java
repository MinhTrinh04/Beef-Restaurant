package com.eshop.BasketService.Controller;



import com.eshop.BasketService.IntegrationEvents.EventHandling.BasketClearedIntegrationEventHandler;
import com.eshop.BasketService.IntegrationEvents.Events.BasketClearedIntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.eshop.BasketService.Constants.BasketConstants.QUEUE_NAME;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = QUEUE_NAME)
public class IntegrationEventController {
    private final BasketClearedIntegrationEventHandler basketClearedEventHandler;

    @RabbitHandler
    public void handleIntegrationEvent(BasketClearedIntegrationEvent event) {
        basketClearedEventHandler.handle(event);
    }

}


