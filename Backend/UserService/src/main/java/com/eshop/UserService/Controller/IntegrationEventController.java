package com.eshop.UserService.Controller;

import com.eshop.UserService.IntegrationEvents.EventHandling.*;
import com.eshop.UserService.IntegrationEvents.Events.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.eshop.UserService.Constant.UserConstants.QUEUE_NAME;

@Component
@Slf4j
@AllArgsConstructor
@RabbitListener(queues = QUEUE_NAME)
public class IntegrationEventController {

    private final OrderCreatedForEmailEventHandler orderCreatedForEmailEventHandler;
    private final OrderPaidForEmailEventHandler orderPaidForEmailEventHandler;
    private final OrderCancelledForEmailEventHandler orderCancelledForEmailEventHandler;

    @RabbitHandler
    public void handleOrderCreatedForEmailEvent(OrderCreatedForEmailEvent event) {
        log.info("📨 Received OrderCreatedForEmailEvent from queue: {}", QUEUE_NAME);
        orderCreatedForEmailEventHandler.handle(event);
    }

    @RabbitHandler
    public void handleOrderPaidForEmailEvent(OrderPaidForEmailEvent event) {
        log.info("📨 Received OrderPaidForEmailEvent from queue: {}", QUEUE_NAME);
        orderPaidForEmailEventHandler.handle(event);
    }

    @RabbitHandler
    public void handleOrderCancelledForEmailEvent(OrderCancelledForEmailEvent event) {
        log.info("📨 Received OrderCancelledForEmailEvent from queue: {}", QUEUE_NAME);
        orderCancelledForEmailEventHandler.handle(event);
    }
}
