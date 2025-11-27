package com.eshop.OrderingService.Controller;

import com.eshop.OrderingService.IntegrationEvents.EventHandling.*;
import com.eshop.OrderingService.IntegrationEvents.Events.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.eshop.OrderingService.Constants.OrderingConstants.QUEUE_NAME;

@Component
@Slf4j
@AllArgsConstructor
@RabbitListener(queues = QUEUE_NAME)
public class IntegrationEventController {

    private final UserCheckoutAcceptedIntegrationEventV2Handler userCheckoutAcceptedV2Handler;
    private final OrderPaymentSucceededIntegrationEventHandler orderPaymentSucceededHandler;
    private final OrderPaymentFailedIntegrationEventHandler orderPaymentFailedHandler;

    @RabbitHandler
    public void handleUserCheckoutAcceptedV2(UserCheckoutAcceptedIntegrationEventV2 event) {
        log.info("📨 Received UserCheckoutAcceptedIntegrationEventV2 from queue: {}",
                QUEUE_NAME);
        userCheckoutAcceptedV2Handler.handle(event);
    }

    @RabbitHandler
    public void handleOrderPaymentSucceeded(OrderPaymentSucceededIntegrationEvent event) {
        log.info("📨 Received OrderPaymentSucceededIntegrationEvent from queue: {}",
                QUEUE_NAME);
        orderPaymentSucceededHandler.handle(event);
    }

    @RabbitHandler
    public void handleOrderPaymentFailed(OrderPaymentFailedIntegrationEvent event) {
        log.info("📨 Received OrderPaymentFailedIntegrationEvent from queue: {}",
                QUEUE_NAME);
        orderPaymentFailedHandler.handle(event);
    }
}
