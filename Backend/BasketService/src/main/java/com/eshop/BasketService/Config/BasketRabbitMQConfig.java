package com.eshop.BasketService.Config;

import com.eshop.buildingblocks.EventBus.Config.ConventionBasedJavaTypeMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.eshop.BasketService.Constants.BasketConstants.*;

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

    @Bean
    public Binding bindingBasketCleared(TopicExchange eventBusExchange, Queue basketServiceQueue) {
        return BindingBuilder.bind(basketServiceQueue).to(eventBusExchange).with(BASKET_CLEARED);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        ConventionBasedJavaTypeMapper typeMapper = new ConventionBasedJavaTypeMapper(
                "com.eshop.BasketService.IntegrationEvents.Events"
        );

        typeMapper.setTrustedPackages("*");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}