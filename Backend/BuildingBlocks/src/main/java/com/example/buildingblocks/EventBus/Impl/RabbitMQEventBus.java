package com.example.buildingblocks.EventBus.Impl;

import com.example.buildingblocks.EventBus.Abstractions.IEventBus;
import com.example.buildingblocks.EventBus.Events.IntegrationEvent;
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

    @Value("${spring.rabbitmq.exchange}")
    private String exchangeName;

    @Override
    public void publish(IntegrationEvent event) {

        String routingKey = event.getClass().getSimpleName();

        log.info("Publishing event: {}. ID: {}. RoutingKey: '{}', Exchange: '{}'",
                routingKey, event.getId(), routingKey, exchangeName);

        rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
    }

}
