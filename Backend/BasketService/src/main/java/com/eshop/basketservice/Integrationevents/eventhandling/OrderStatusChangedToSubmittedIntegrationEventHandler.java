package com.eshop.basketservice.Integrationevents.eventhandling;

import com.eshop.basketservice.Integrationevents.Events.OrderStatusChangedToSubmittedIntegrationEvent;
import com.eshop.basketservice.Repository.BasketRepository;
import com.fasterxml.jackson.databind.ObjectMapper; // Import mới
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.LinkedHashMap; // Import mới

@Component
@RequiredArgsConstructor
public class OrderStatusChangedToSubmittedIntegrationEventHandler {

    private final BasketRepository basketRepository;
    private final ObjectMapper objectMapper; // Spring sẽ tự động inject bean này
    private static final Logger logger = LoggerFactory.getLogger(OrderStatusChangedToSubmittedIntegrationEventHandler.class);

    /**
     * --- THAY ĐỔI QUAN TRỌNG ---
     * Phương thức handle bây giờ chấp nhận LinkedHashMap,
     * đúng với kiểu dữ liệu mà RabbitMQ listener gửi đến khi không có TypeId.
     */
    public void handle(LinkedHashMap<String, Object> message) {
        // Tự tay chuyển đổi Map thành đối tượng Event cụ thể mà chúng ta muốn.
        final OrderStatusChangedToSubmittedIntegrationEvent event = objectMapper.convertValue(message, OrderStatusChangedToSubmittedIntegrationEvent.class);

        // Phần logic nghiệp vụ giữ nguyên như cũ.
        logger.info("Handling integration event: {} ({})", event.getId(), event.getClass().getSimpleName());
        logger.info("Deleting basket for buyer: {}", event.getBuyerId());
        basketRepository.deleteById(event.getBuyerId());
    }
}