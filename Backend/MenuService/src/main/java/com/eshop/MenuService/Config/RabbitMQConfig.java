package com.eshop.MenuService.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "menu-service-queue";

    /**
     * Khai báo một Queue (hàng đợi) riêng cho MenuService.
     * durable = true: Hàng đợi sẽ không bị mất khi RabbitMQ khởi động lại,
     * đảm bảo không message nào bị thất lạc.
     */
    @Bean
    public Queue menuServiceQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Tạo một Binding (liên kết) giữa queue của MenuService và exchange trung tâm.
     * Liên kết này đăng ký lắng nghe sự kiện "OrderStatusChangedToPaidIntegrationEvent".
     * @param eventBusExchange Bean này được cung cấp từ module BuildingBlocks.
     */
    @Bean
    public Binding bindingOrderStatusChangedToPaid(TopicExchange eventBusExchange, Queue menuServiceQueue) {
        return BindingBuilder.bind(menuServiceQueue)
                .to(eventBusExchange)
                .with("OrderStatusChangedToPaidIntegrationEvent"); // routingKey phải khớp tên lớp Event
    }

    /**
     * Tạo một Binding khác để đăng ký lắng nghe sự kiện
     * "OrderStatusChangedToAwaitingStockValidationIntegrationEvent".
     */
    @Bean
    public Binding bindingOrderStatusChangedToAwaitingStockValidation(TopicExchange eventBusExchange, Queue menuServiceQueue) {
        return BindingBuilder.bind(menuServiceQueue)
                .to(eventBusExchange)
                .with("OrderStatusChangedToAwaitingStockValidationIntegrationEvent");
    }
}
