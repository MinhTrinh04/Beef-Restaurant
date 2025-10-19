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

@Configuration
public class BasketRabbitMQConfig {

    /**
     * Khai báo một Queue để BasketService lắng nghe sự kiện Order đã được tạo.
     * Tên queue là "basket_order_submitted_queue".
     */
    @Bean
    public Queue orderSubmittedQueue() {
        // durable: true -> Hàng đợi sẽ không bị mất khi RabbitMQ khởi động lại.
        return new Queue("basket_order_submitted_queue", true);
    }

    /**
     * Tạo một Binding để kết nối Queue ở trên với TopicExchange chung của hệ thống.
     * Bất kỳ message nào có routing key là "order.submitted" sẽ được gửi vào queue này.
     * Spring sẽ tự động inject bean 'topicExchange' đã được định nghĩa trong building-blocks.
     */
    @Bean
    public Binding binding(Queue orderSubmittedQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(orderSubmittedQueue).to(topicExchange).with("order.submitted");
    }

    /**
     * Tạo một MessageListenerAdapter để "bọc" lấy handler xử lý logic của chúng ta.
     * Nó sẽ sử dụng MessageConverter để "dịch" tin nhắn JSON thành object trước khi gọi handler.
     * @param handler Bean OrderStatusChangedToSubmittedIntegrationEventHandler sẽ được Spring tự động inject vào.
     * @param messageConverter Bean MessageConverter được định nghĩa chung trong module building-blocks.
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(OrderStatusChangedToSubmittedIntegrationEventHandler handler, MessageConverter messageConverter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(handler, "handle");
        adapter.setMessageConverter(messageConverter);
        return adapter;
    }

    /**
     * Tạo một Container để thực sự lắng nghe tin nhắn từ RabbitMQ.
     * Đây là thành phần chính kết nối mọi thứ lại với nhau.
     * @param connectionFactory Bean ConnectionFactory do Spring AMQP cung cấp.
     * @param listenerAdapter Bean MessageListenerAdapter chúng ta đã định nghĩa ở trên.
     */
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(orderSubmittedQueue().getName()); // Lắng nghe trên queue đã khai báo.
        container.setMessageListener(listenerAdapter); // Sử dụng adapter để xử lý message.
        return container;
    }
}