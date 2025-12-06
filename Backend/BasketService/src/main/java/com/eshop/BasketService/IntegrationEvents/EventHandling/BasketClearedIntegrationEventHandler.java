package com.eshop.BasketService.IntegrationEvents.EventHandling;

import com.eshop.BasketService.Exception.BasketNotFoundException;
import com.eshop.BasketService.IntegrationEvents.Events.BasketClearedIntegrationEvent;
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
public class BasketClearedIntegrationEventHandler implements IIntegrationEventHandler<BasketClearedIntegrationEvent> {

    private final BasketRepository basketRepository;

    @Override
    @Transactional
    public void handle(BasketClearedIntegrationEvent event) {
        log.info("🗑️ BasketClearedIntegrationEvent received for BuyerId: {}", event.getBuyerId());
        Optional<Basket> basket = basketRepository.findById(event.getBuyerId());
        if (basket.isEmpty()) {
            throw new BasketNotFoundException("Basket", "buyerId", event.getBuyerId());
        }
        basketRepository.deleteById(event.getBuyerId());
        log.info("✅ Basket cleared for buyer: {}", event.getBuyerId());
    }
}