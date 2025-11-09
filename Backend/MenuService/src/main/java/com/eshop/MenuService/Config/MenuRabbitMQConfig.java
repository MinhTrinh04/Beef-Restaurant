package com.eshop.MenuService.Config;

import com.eshop.buildingblocks.EventBus.Config.ConventionBasedJavaTypeMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.eshop.MenuService.Constants.MenuConstants.*;

@Configuration
public class MenuRabbitMQConfig {

    @Bean
    public Queue menuServiceQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingOrderStatusChangedToPaid(TopicExchange eventBusExchange, Queue menuServiceQueue) {
        return BindingBuilder.bind(menuServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_STATUS_CHANGE_TO_PAID_INTEGRATION_EVENT);
    }

    @Bean
    public Binding bindingOrderStatusChangedToAwaitingStockValidation(TopicExchange eventBusExchange, Queue menuServiceQueue) {
        return BindingBuilder.bind(menuServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_STATUS_CHANGE_TO_AWAITING_STOCK_VALIDATION_INTEGRATION_EVENT);
    }

    @Bean
    public Binding bindingOrderStatusChangedToPaidV2(TopicExchange eventBusExchange, Queue menuServiceQueue) {
        return BindingBuilder.bind(menuServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_STATUS_CHANGE_TO_PAID_INTEGRATION_EVENT_V2);
    }

    @Bean
    public Binding bindingOrderStatusChangedToCancelled(TopicExchange eventBusExchange, Queue menuServiceQueue) {
        return BindingBuilder.bind(menuServiceQueue)
                .to(eventBusExchange)
                .with(ORDER_STATUS_CHANGE_TO_CANCELLED_INTEGRATION_EVENT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        ConventionBasedJavaTypeMapper typeMapper = new ConventionBasedJavaTypeMapper(
                "com.eshop.MenuService.IntegrationEvents.Events"
        );

        typeMapper.setTrustedPackages("*");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
