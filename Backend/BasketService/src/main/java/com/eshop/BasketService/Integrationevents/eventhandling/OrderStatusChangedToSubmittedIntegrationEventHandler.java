package com.eshop.BasketService.Integrationevents.eventhandling;

import com.eshop.BasketService.Exception.BasketNotFoundException;
import com.eshop.BasketService.Integrationevents.Events.OrderStatusChangedToSubmittedIntegrationEvent;
import com.eshop.BasketService.Model.Basket;
import com.eshop.BasketService.Repository.BasketRepository;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusChangedToSubmittedIntegrationEventHandler implements IIntegrationEventHandler<OrderStatusChangedToSubmittedIntegrationEvent> {

    private final BasketRepository basketRepository;

    @Override
    @Transactional
    public void handle(OrderStatusChangedToSubmittedIntegrationEvent event) {
        log.info("⏳ OrderStatusChangedToSubmittedIntegrationEvent received for OrderId: {}", event.getOrderId());
        Optional<Basket> basket = basketRepository.findById(event.getBuyerId());
        if (basket.isEmpty()) {
            throw new BasketNotFoundException("Basket", "buyerId", event.getBuyerId());
        }
        basketRepository.deleteById(event.getBuyerId());
        log.info("Deleted basket for buyer: {}", event.getBuyerId());
    }
}