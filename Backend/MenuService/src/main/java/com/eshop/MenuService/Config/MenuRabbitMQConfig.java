package com.eshop.MenuService.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
}
