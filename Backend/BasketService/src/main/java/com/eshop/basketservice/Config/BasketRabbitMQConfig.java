package com.eshop.basketservice.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.eshop.basketservice.Constants.BasketConstants.ORDER_STATUS_CHANGE_TO_SUBMITTED;
import static com.eshop.basketservice.Constants.BasketConstants.QUEUE_NAME;

@Configuration
public class BasketRabbitMQConfig {

    @Bean
    public Queue basketServiceQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingOrderStatusChangedToSubmitted(TopicExchange eventBusExchange, Queue basketServiceQueue) {
        return BindingBuilder.bind(basketServiceQueue).to(eventBusExchange).with(ORDER_STATUS_CHANGE_TO_SUBMITTED);
    }

}