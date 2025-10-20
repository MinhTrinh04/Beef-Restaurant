package com.eshop.basketservice.Config;

import com.eshop.basketservice.Integrationevents.eventhandling.OrderStatusChangedToSubmittedIntegrationEventHandler;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.eshop.basketservice.Constants.BasketConstants.ORDER_STATUS_CHANGE_TO_SUBMITTED;
import static com.eshop.basketservice.Constants.BasketConstants.QUEUE_NAME;

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
    public MessageListenerAdapter listenerAdapter(OrderStatusChangedToSubmittedIntegrationEventHandler handler, MessageConverter messageConverter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(handler, "handle");
        adapter.setMessageConverter(messageConverter);
        return adapter;
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(); // Lắng nghe trên queue đã khai báo.
        container.setMessageListener(listenerAdapter); // Sử dụng adapter để xử lý message.
        return container;
    }
}