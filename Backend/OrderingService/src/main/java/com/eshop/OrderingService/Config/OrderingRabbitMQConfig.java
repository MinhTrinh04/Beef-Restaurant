package com.eshop.OrderingService.Config;

import com.eshop.OrderingService.IntegrationEvents.EventHandling.*;
import com.eshop.OrderingService.IntegrationEvents.Events.*;
import com.eshop.buildingblocks.EventBus.Config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.eshop.OrderingService.Constants.OrderingConstants.QUEUE_NAME;

@Configuration
@Import(RabbitMQConfig.class)
@Slf4j
public class OrderingRabbitMQConfig {

    // Exchange names
    public static final String ORDERING_EXCHANGE = "ordering.exchange";
    public static final String BASKET_EXCHANGE = "basket.exchange";
    public static final String MENU_EXCHANGE = "menu.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    // Queue names
    public static final String USER_CHECKOUT_ACCEPTED_QUEUE = "ordering.user.checkout.accepted.queue";
    public static final String ORDER_STOCK_CONFIRMED_QUEUE = "ordering.stock.confirmed.queue";
    public static final String ORDER_STOCK_REJECTED_QUEUE = "ordering.stock.rejected.queue";
    public static final String ORDER_PAYMENT_SUCCEEDED_QUEUE = "ordering.payment.succeeded.queue";
    public static final String ORDER_PAYMENT_FAILED_QUEUE = "ordering.payment.failed.queue";

    // Routing keys
    public static final String USER_CHECKOUT_ACCEPTED_ROUTING_KEY = "user.checkout.accepted";
    public static final String ORDER_STOCK_CONFIRMED_ROUTING_KEY = "order.stock.confirmed";
    public static final String ORDER_STOCK_REJECTED_ROUTING_KEY = "order.stock.rejected";
    public static final String ORDER_PAYMENT_SUCCEEDED_ROUTING_KEY = "order.payment.succeeded";
    public static final String ORDER_PAYMENT_FAILED_ROUTING_KEY = "order.payment.failed";

    // Declare exchanges
    @Bean
    public TopicExchange orderingExchange() {
        return new TopicExchange(ORDERING_EXCHANGE);
    }

    @Bean
    public TopicExchange basketExchange() {
        return new TopicExchange(BASKET_EXCHANGE);
    }

    @Bean
    public TopicExchange menuExchange() {
        return new TopicExchange(MENU_EXCHANGE);
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    // Declare queues
    @Bean
    public Queue userCheckoutAcceptedQueue() {
        return QueueBuilder.durable(USER_CHECKOUT_ACCEPTED_QUEUE).build();
    }

    @Bean
    public Queue orderStockConfirmedQueue() {
        return QueueBuilder.durable(ORDER_STOCK_CONFIRMED_QUEUE).build();
    }

    @Bean
    public Queue orderStockRejectedQueue() {
        return QueueBuilder.durable(ORDER_STOCK_REJECTED_QUEUE).build();
    }

    @Bean
    public Queue orderPaymentSucceededQueue() {
        return QueueBuilder.durable(ORDER_PAYMENT_SUCCEEDED_QUEUE).build();
    }

    @Bean
    public Queue orderPaymentFailedQueue() {
        return QueueBuilder.durable(ORDER_PAYMENT_FAILED_QUEUE).build();
    }

    @Bean
    public Queue orderServiceQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    // Declare bindings
    @Bean
    public Binding userCheckoutAcceptedBinding(TopicExchange eventBusExchange) {
        return BindingBuilder
                .bind(orderServiceQueue())
                .to(eventBusExchange)
                .with("UserCheckoutAcceptedIntegrationEvent");
    }

    @Bean
    public Binding orderStockConfirmedBinding() {
        return BindingBuilder
                .bind(orderStockConfirmedQueue())
                .to(menuExchange())
                .with(ORDER_STOCK_CONFIRMED_ROUTING_KEY);
    }

    @Bean
    public Binding orderStockRejectedBinding() {
        return BindingBuilder
                .bind(orderStockRejectedQueue())
                .to(menuExchange())
                .with(ORDER_STOCK_REJECTED_ROUTING_KEY);
    }

    @Bean
    public Binding orderPaymentSucceededBinding() {
        return BindingBuilder
                .bind(orderPaymentSucceededQueue())
                .to(paymentExchange())
                .with(ORDER_PAYMENT_SUCCEEDED_ROUTING_KEY);
    }

    @Bean
    public Binding orderPaymentFailedBinding() {
        return BindingBuilder
                .bind(orderPaymentFailedQueue())
                .to(paymentExchange())
                .with(ORDER_PAYMENT_FAILED_ROUTING_KEY);
    }
}
