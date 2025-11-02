package com.eshop.buildingblocks.EventBus.Impl;

import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import com.eshop.buildingblocks.EventBus.Config.RabbitMQConfig;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.RequiredArgsConstructor;
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
}
