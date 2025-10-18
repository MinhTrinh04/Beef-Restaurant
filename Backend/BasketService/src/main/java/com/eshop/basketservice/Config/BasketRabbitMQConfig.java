package com.eshop.basketservice.Config;

import com.eshop.basketservice.Integrationevents.eventhandling.OrderStatusChangedToSubmittedIntegrationEventHandler;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BasketRabbitMQConfig {
    private final IEventBus eventBus;
    private final OrderStatusChangedToSubmittedIntegrationEventHandler handler;

    // Queue để lắng nghe sự kiện Order đã được submitted
    @Bean
    public Queue orderSubmittedQueue() {
        return new Queue("basket_order_submitted_queue", true);
    }

    // Binding queue này với exchange
    @Bean
    public Binding binding(Queue orderSubmittedQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(orderSubmittedQueue).to(topicExchange).with("order.submitted");
    }

    // Tự động subscribe handler vào event bus khi service khởi động
    @PostConstruct
    public void configureEventBus() {
//        eventBus.subscribe(
//                "order.submitted",
//                "basket_order_submitted_queue",
//                handler);
    }
}