package com.eshop.buildingblocks.EventBus.Config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "eshop_event_bus";

    @Bean
    public TopicExchange eventBusExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    @ConditionalOnMissingBean(MessageConverter.class)
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        typeMapper.setTrustedPackages("*");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}