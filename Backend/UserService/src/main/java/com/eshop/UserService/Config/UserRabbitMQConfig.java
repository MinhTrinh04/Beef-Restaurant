package com.eshop.UserService.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.eshop.UserService.Constant.UserConstants.QUEUE_NAME;
import static com.eshop.UserService.Constant.UserConstants.THANK_YOU_ORDER_EMAIL_EVENT;

@Configuration
public class UserRabbitMQConfig {

    @Bean
    public Queue userServiceQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding orderPaymentSucceededBinding(
            Queue userServiceQueue,
            TopicExchange eventBusExchange) {
        return BindingBuilder.bind(userServiceQueue)
                .to(eventBusExchange)
                .with(THANK_YOU_ORDER_EMAIL_EVENT);
    }



}
