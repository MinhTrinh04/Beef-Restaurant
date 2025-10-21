package com.eshop.basketservice.Integrationevents.eventhandling;

import com.eshop.basketservice.Exception.BasketNotFoundException;
import com.eshop.basketservice.Integrationevents.Events.OrderStatusChangedToSubmittedIntegrationEvent;
import com.eshop.basketservice.Model.Basket;
import com.eshop.basketservice.Repository.IBasketRepository;
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

    private final IBasketRepository IBasketRepository;

    @Override
    @Transactional
    public void handle(OrderStatusChangedToSubmittedIntegrationEvent event) {
        log.info("⏳ OrderStatusChangedToSubmittedIntegrationEvent received for OrderId: {}", event.getOrderId());
        Optional<Basket> basket = IBasketRepository.findById(event.getBuyerId());
        if (basket.isEmpty()) {
            throw new BasketNotFoundException("Basket", "buyerId", event.getBuyerId());
        }
        IBasketRepository.deleteById(event.getBuyerId());
        log.info("Deleted basket for buyer: {}", event.getBuyerId());
    }
}