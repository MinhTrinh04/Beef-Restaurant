package com.eshop.basketservice.Integrationevents.eventhandling;

import com.eshop.basketservice.Integrationevents.Events.OrderStatusChangedToSubmittedIntegrationEvent;
import com.eshop.basketservice.Repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStatusChangedToSubmittedIntegrationEventHandler {

    private final BasketRepository basketRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderStatusChangedToSubmittedIntegrationEventHandler.class);

    /**
     * --- PHIÊN BẢN CHUẨN ---
     * Phương thức này nhận trực tiếp đối tượng Event đã được MessageConverter dịch sẵn.
     */
    public void handle(OrderStatusChangedToSubmittedIntegrationEvent event) {
        logger.info("Handling integration event: {} ({})", event.getId(), event.getClass().getSimpleName());
        logger.info("Deleting basket for buyer: {}", event.getBuyerId());
        basketRepository.deleteById(event.getBuyerId());
    }
}