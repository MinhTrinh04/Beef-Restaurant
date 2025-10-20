package com.eshop.OrderingService.Config;

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
public class OrderingRabbitMQListener {

    private final UserCheckoutAcceptedIntegrationEventHandler userCheckoutAcceptedHandler;
    private final OrderStockConfirmedIntegrationEventHandler orderStockConfirmedHandler;
    private final OrderStockRejectedIntegrationEventHandler orderStockRejectedHandler;
    private final OrderPaymentSucceededIntegrationEventHandler orderPaymentSucceededHandler;
    private final OrderPaymentFailedIntegrationEventHandler orderPaymentFailedHandler;

    @RabbitHandler
    public void handleUserCheckoutAccepted(UserCheckoutAcceptedIntegrationEvent event) {
        log.info("📨 Received UserCheckoutAcceptedIntegrationEvent from queue: {}",
                QUEUE_NAME);
        userCheckoutAcceptedHandler.handle(event);
    }

    @RabbitHandler
    public void handleOrderStockConfirmed(OrderStockConfirmedIntegrationEvent event) {
        log.info("📨 Received OrderStockConfirmedIntegrationEvent from queue: {}",
                QUEUE_NAME);
        orderStockConfirmedHandler.handle(event);
    }

    @RabbitListener
    public void handleOrderStockRejected(OrderStockRejectedIntegrationEvent event) {
        log.info("📨 Received OrderStockRejectedIntegrationEvent from queue: {}",
                QUEUE_NAME);
        orderStockRejectedHandler.handle(event);
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
