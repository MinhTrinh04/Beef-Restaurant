package com.eshop.basketservice.Service.Impl;

import com.eshop.basketservice.Exception.BasketNotFoundException;
import com.eshop.basketservice.Integrationevents.Events.UserCheckoutAcceptedIntegrationEvent;
import com.eshop.basketservice.Model.Basket;
import com.eshop.basketservice.Repository.BasketRepository;
import com.eshop.basketservice.Service.IBasketService;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eshop.basketservice.Model.BasketCheckout;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class BasketService implements IBasketService {
    private final BasketRepository basketRepository;
    private final IEventBus eventBus;

    @Override
    public Basket getBasketById(String id) {
        Basket existingBasket = basketRepository.findById(id).orElseThrow(() -> new BasketNotFoundException("Basket", "buyerId", id));
        return existingBasket;
    }

    @Override
    @Transactional
    public boolean updateBasket(Basket basket) {
        Basket resBasket = basketRepository.save(basket);
        if (resBasket == null){
            log.error("Error updating/creating basket for buyerId: {}", basket.getBuyerId());
            return false;
        }
        log.info("Basket updated/created successfully for buyerId: {}", basket.getBuyerId());
        return true;

    }

    @Override
    @Transactional
    public boolean deleteBasket(String id) {
        basketRepository.deleteById(id);
        return true;
    }

    @Override
    public void checkout(String buyerId, BasketCheckout basketCheckout, String requestId) {
        Basket basket = basketRepository.findById(buyerId)
                .orElseThrow(() -> new BasketNotFoundException("Basket", "buyerId" , buyerId));

        UUID eventRequestId;
        try {
            eventRequestId = UUID.fromString(requestId);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Invalid or missing X-Request-Id. Generating new ID.");
            eventRequestId = UUID.randomUUID();
        }

        UserCheckoutAcceptedIntegrationEvent eventMessage = new UserCheckoutAcceptedIntegrationEvent(
                buyerId,
                basketCheckout.getUserEmail(),
                basketCheckout.getCity(),
                basketCheckout.getStreet(),
                basketCheckout.getState(),
                basketCheckout.getCountry(),
                eventRequestId,
                basket
        );

        try {
            eventBus.publish(eventMessage);
            log.info("✅ Publishing UserCheckoutAcceptedIntegrationEvent for buyerId {}", buyerId);
        } catch (Exception e) {
            log.error("❌ Error publishing UserCheckoutAcceptedIntegrationEvent for buyerId {}", buyerId);
            throw new RuntimeException("Error publishing checkout event", e);
        }
    }
}
