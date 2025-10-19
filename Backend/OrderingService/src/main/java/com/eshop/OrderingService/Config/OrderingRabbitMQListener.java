package com.eshop.OrderingService.Config;

import com.eshop.OrderingService.IntegrationEvents.EventHandling.*;
import com.eshop.OrderingService.IntegrationEvents.Events.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class OrderingRabbitMQListener {

    private final UserCheckoutAcceptedIntegrationEventHandler userCheckoutAcceptedHandler;
    private final OrderStockConfirmedIntegrationEventHandler orderStockConfirmedHandler;
    private final OrderStockRejectedIntegrationEventHandler orderStockRejectedHandler;
    private final OrderPaymentSucceededIntegrationEventHandler orderPaymentSucceededHandler;
    private final OrderPaymentFailedIntegrationEventHandler orderPaymentFailedHandler;

    @RabbitListener(queues = OrderingRabbitMQConfig.USER_CHECKOUT_ACCEPTED_QUEUE)
    public void handleUserCheckoutAccepted(UserCheckoutAcceptedIntegrationEvent event) {
        log.info("📨 Received UserCheckoutAcceptedIntegrationEvent from queue: {}",
                OrderingRabbitMQConfig.USER_CHECKOUT_ACCEPTED_QUEUE);
        userCheckoutAcceptedHandler.handle(event);
    }

    @RabbitListener(queues = OrderingRabbitMQConfig.ORDER_STOCK_CONFIRMED_QUEUE)
    public void handleOrderStockConfirmed(OrderStockConfirmedIntegrationEvent event) {
        log.info("📨 Received OrderStockConfirmedIntegrationEvent from queue: {}",
                OrderingRabbitMQConfig.ORDER_STOCK_CONFIRMED_QUEUE);
        orderStockConfirmedHandler.handle(event);
    }

    @RabbitListener(queues = OrderingRabbitMQConfig.ORDER_STOCK_REJECTED_QUEUE)
    public void handleOrderStockRejected(OrderStockRejectedIntegrationEvent event) {
        log.info("📨 Received OrderStockRejectedIntegrationEvent from queue: {}",
                OrderingRabbitMQConfig.ORDER_STOCK_REJECTED_QUEUE);
        orderStockRejectedHandler.handle(event);
    }

    @RabbitListener(queues = OrderingRabbitMQConfig.ORDER_PAYMENT_SUCCEEDED_QUEUE)
    public void handleOrderPaymentSucceeded(OrderPaymentSucceededIntegrationEvent event) {
        log.info("📨 Received OrderPaymentSucceededIntegrationEvent from queue: {}",
                OrderingRabbitMQConfig.ORDER_PAYMENT_SUCCEEDED_QUEUE);
        orderPaymentSucceededHandler.handle(event);
    }

    @RabbitListener(queues = OrderingRabbitMQConfig.ORDER_PAYMENT_FAILED_QUEUE)
    public void handleOrderPaymentFailed(OrderPaymentFailedIntegrationEvent event) {
        log.info("📨 Received OrderPaymentFailedIntegrationEvent from queue: {}",
                OrderingRabbitMQConfig.ORDER_PAYMENT_FAILED_QUEUE);
        orderPaymentFailedHandler.handle(event);
    }
}
