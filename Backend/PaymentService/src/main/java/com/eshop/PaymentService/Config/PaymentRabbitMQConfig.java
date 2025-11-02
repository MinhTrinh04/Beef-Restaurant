package com.eshop.PaymentService.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.eshop.PaymentService.Constants.PaymentConstants.QUEUE_NAME;

@Configuration
public class PaymentRabbitMQConfig {

    @Bean
    public Queue paymentServiceQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingOrderStatusChangedToValidatedIntegrationEvent(TopicExchange eventBusExchange, Queue paymentServiceQueue) {
        return BindingBuilder.bind(paymentServiceQueue)
                .to(eventBusExchange)
                .with("OrderStatusChangedToValidatedIntegrationEvent");
    }
}