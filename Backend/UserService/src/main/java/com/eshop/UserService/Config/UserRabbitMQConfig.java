package com.eshop.UserService.Config;

import com.eshop.buildingblocks.EventBus.Config.ConventionBasedJavaTypeMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.eshop.UserService.Constant.UserConstants.*;

@Configuration
public class UserRabbitMQConfig {

    @Bean
    public Queue userServiceQueue() {
        return new Queue(QUEUE_NAME, true);
    }


    @Bean
    public Binding orderCreatedForEmailBinding(
            Queue userServiceQueue,
            TopicExchange eventBusExchange) {
        return BindingBuilder.bind(userServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_CREATED_FOR_EMAIL_EVENT);
    }

    @Bean
    public Binding orderPaidForEmailBinding(
            Queue userServiceQueue,
            TopicExchange eventBusExchange) {
        return BindingBuilder.bind(userServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_PAID_FOR_EMAIL_EVENT);
    }

    @Bean
    public Binding orderCancelledForEmailBinding(
            Queue userServiceQueue,
            TopicExchange eventBusExchange) {
        return BindingBuilder.bind(userServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_CANCELLED_FOR_EMAIL_EVENT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // Sử dụng mapper tùy chỉnh
        ConventionBasedJavaTypeMapper typeMapper = new ConventionBasedJavaTypeMapper(
                "com.eshop.UserService.IntegrationEvents.Events");

        // Tin tưởng tất cả các gói
        typeMapper.setTrustedPackages("*");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
