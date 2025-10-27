package com.eshop.OrderingService.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
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
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // 1. Sử dụng mapper tùy chỉnh
        ConventionBasedJavaTypeMapper typeMapper = new ConventionBasedJavaTypeMapper(
                "com.eshop.OrderingService.IntegrationEvents.Events"
        );

        // 2. Tin tưởng tất cả các gói (để chấp nhận message từ BasketService)
        typeMapper.setTrustedPackages("*");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
