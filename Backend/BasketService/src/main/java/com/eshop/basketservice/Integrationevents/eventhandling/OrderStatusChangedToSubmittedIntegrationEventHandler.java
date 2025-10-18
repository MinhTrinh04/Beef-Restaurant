package com.eshop.basketservice.Integrationevents.eventhandling;
import com.eshop.basketservice.Integrationevents.Events.OrderStatusChangedToSubmittedIntegrationEvent;
import com.eshop.basketservice.Repository.BasketRepository;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStatusChangedToSubmittedIntegrationEventHandler implements IIntegrationEventHandler<OrderStatusChangedToSubmittedIntegrationEvent> {

    private final BasketRepository basketRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderStatusChangedToSubmittedIntegrationEventHandler.class);

    @Override
    public void handle(OrderStatusChangedToSubmittedIntegrationEvent event) {
        logger.info("Handling integration event: {} ({})", event.getId(), event.getClass().getSimpleName());
        basketRepository.deleteById(event.getBuyerId());
    }
}