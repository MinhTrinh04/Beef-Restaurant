package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.IntegrationEvents.Events.UserCheckoutAcceptedIntegrationEvent;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserCheckoutAcceptedIntegrationEventHandler
        implements IIntegrationEventHandler<UserCheckoutAcceptedIntegrationEvent> {

    private final IOrderingService orderingService;

    @Override
    public void handle(UserCheckoutAcceptedIntegrationEvent event) {
        log.info("🛒 UserCheckoutAcceptedIntegrationEvent received for UserId: {}", event.getUserId());

        try {
            // Create order from checkout event
            orderingService.createOrderFromCheckout(event);
            log.info("✅ Order created successfully for UserId: {}", event.getUserId());
        } catch (Exception e) {
            log.error("❌ Failed to create order for UserId: {}. Error: {}", event.getUserId(), e.getMessage(), e);
        }
    }
}
