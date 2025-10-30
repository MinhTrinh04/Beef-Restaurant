package com.eshop.BasketService.Service.Impl;

import com.eshop.BasketService.DTO.StockValidationItem;
import com.eshop.BasketService.Exception.BasketNotFoundException;
import com.eshop.BasketService.Exception.StockValidationException;
import com.eshop.BasketService.IntegrationEvents.Events.UserCheckoutAcceptedIntegrationEvent;
import com.eshop.BasketService.IntegrationEvents.Events.UserCheckoutAcceptedIntegrationEventV2;
import com.eshop.BasketService.Model.Basket;
import com.eshop.BasketService.Repository.BasketRepository;
import com.eshop.BasketService.Service.IBasketService;
import com.eshop.BasketService.Service.client.MenuServiceClient;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eshop.BasketService.Model.BasketCheckout;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BasketService implements IBasketService {
    private final BasketRepository basketRepository;
    private final IEventBus eventBus;
    private final MenuServiceClient menuServiceClient;

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

    @Override
    public void checkoutV2(String buyerId, BasketCheckout basketCheckout, String requestId) {
        Basket basket = basketRepository.findById(buyerId)
                .orElseThrow(() -> new BasketNotFoundException("Basket", "buyerId" , buyerId));
        log.info("BƯỚC 2: Đã tìm thấy giỏ hàng. Tổng số items: {}. Chuẩn bị map sang List<StockValidationItem>.", basket.getItems().size());
        // Tạo request cho Pre-check
        List<StockValidationItem> validationRequest = basket.getItems().stream()
                .map(item -> {
                    log.info("...Đang map item: ProductId=[{}], Units=[{}]", item.getProductId(), item.getUnits());

                    if (item.getUnits() == null) {
                        log.warn("CẢNH BÁO: Item với ProductId [{}] có 'units' BỊ NULL trong Redis!", item.getProductId());
                    }

                    return new StockValidationItem(item.getProductId(), item.getUnits());
                })
                .collect(Collectors.toList());

        // Gọi Feign Client (Pre-check đồng bộ)
        try {
            menuServiceClient.validateStock(validationRequest);
        } catch (FeignException.BadRequest e) {
            throw new StockValidationException("Stock validation failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Menu service is unavailable: " + e.getMessage());
        }

        UUID eventRequestId;
        try {
            eventRequestId = UUID.fromString(requestId);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Invalid or missing X-Request-Id. Generating new ID.");
            eventRequestId = UUID.randomUUID();
        }

        // Nếu Pre-check thành công, dùng event mới để ko ảnh hướng tới luồng cũ
        UserCheckoutAcceptedIntegrationEventV2 event = new UserCheckoutAcceptedIntegrationEventV2(
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
            eventBus.publish(event);
            log.info("✅ Publishing UserCheckoutAcceptedIntegrationEventV2 for buyerId {}", buyerId);
        } catch (Exception e) {
            log.error("❌ Error publishing UserCheckoutAcceptedIntegrationEventV2 for buyerId {}", buyerId);
            throw new RuntimeException("Failed to publish checkout event: " + e.getMessage());
        }
    }
}
