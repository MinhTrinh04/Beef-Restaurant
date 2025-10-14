package com.eshop.buildingblocks.EventBus.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "eshop_event_bus";

    @Bean
    public TopicExchange eventBusExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper();
//    }

    /**
     * Cung cấp một bean MessageConverter để tự động chuyển đổi
     * các đối tượng Event (POJO) sang JSON và ngược lại.
     * Điều này giúp code ở publisher và listener sạch sẽ hơn rất nhiều.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
