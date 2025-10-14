package com.eshop.buildingblocks.EventBus.Impl;

import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import com.eshop.buildingblocks.EventBus.Config.RabbitMQConfig;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQEventBus implements IEventBus {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(IntegrationEvent event) {
        String routingKey = event.getClass().getSimpleName();
        log.info("Publishing event to RabbitMQ. Exchange: {}, RoutingKey: {}. Event ID: {}", RabbitMQConfig.EXCHANGE_NAME, routingKey, event.getId());
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, routingKey, event);
        } catch (Exception e) {
            log.error("Error publishing event with ID: {}", event.getId(), e);
        }
    }

    /**
     * Việc subscribe giờ đây sẽ được xử lý ở tầng service thông qua @RabbitListener.
     * IEventBus không còn chịu trách nhiệm quản lý subscriptions nữa,
     * giúp nó tập trung hoàn toàn vào việc publish.
     *
     * @param eventType The type of the event to subscribe to.
     * @param handler The handler for the event.
     * @param <T> The type of the integration event.
     */
    @Override
    public <T extends IntegrationEvent> void subscribe(Class<T> eventType, IIntegrationEventHandler<T> handler) {
        log.warn("Subscribe method is not implemented in RabbitMQEventBus. " +
                "Use @RabbitListener in your service modules instead to subscribe to events.");

    }

}
