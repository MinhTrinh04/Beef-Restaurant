package com.eshop.OrderingService.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.eshop.OrderingService.Constants.OrderingConstants.*;

@Configuration
@Slf4j
public class OrderingRabbitMQConfig {

    @Bean
    public Queue orderServiceQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingUserCheckoutAccepted(TopicExchange eventBusExchange, Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(eventBusExchange)
                .with(USER_CHECKOUT_ACCEPTED_EVENT);
    }

    @Bean
    public Binding bindingUserCheckoutAcceptedV2(TopicExchange eventBusExchange, Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(eventBusExchange)
                .with(USER_CHECKOUT_ACCEPTED_EVENT_V2);
    }

    @Bean
    public Binding bindingOrderStockConfirmed(TopicExchange eventBusExchange, Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_STOCK_CONFIRMED_EVENT);
    }

    @Bean
    public Binding bindingOrderStockRejected(TopicExchange eventBusExchange, Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_STOCK_REJECTED_EVENT);
    }

    @Bean
    public Binding bindingOrderPaymentSucceeded(TopicExchange eventBusExchange, Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_PAYMENT_SUCCEEDED_EVENT);
    }

    @Bean
    public Binding bindingOrderPaymentFailed(TopicExchange eventBusExchange, Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_PAYMENT_FAILED_EVENT);
    }

    @Bean
    public Binding bindingOrderPaymentCancelled(TopicExchange eventBusExchange, Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_PAYMENT_CANCELLED_EVENT);
    }

    @Bean
    public Binding bindingOrderPaymentSucceededNotification(TopicExchange eventBusExchange, Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_PAYMENT_SUCCEEDED_NOTIFICATION_EVENT);
    }

    @Bean
    public Binding bindingOrderCreatedWithPaymentLinkNotification(TopicExchange eventBusExchange,
            Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_CREATED_WITH_PAYMENT_LINK_NOTIFICATION_EVENT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // Use custom mapper that supports events from multiple services
        OrderingServiceTypeMapper typeMapper = new OrderingServiceTypeMapper();

        // Trust all packages
        typeMapper.setTrustedPackages("*");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
