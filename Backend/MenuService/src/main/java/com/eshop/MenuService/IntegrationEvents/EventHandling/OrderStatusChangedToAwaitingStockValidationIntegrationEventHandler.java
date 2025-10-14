package com.eshop.MenuService.IntegrationEvents.EventHandling;

import com.eshop.MenuService.Exception.ResourceNotFoundException;
import com.eshop.MenuService.IntegrationEvents.Events.*;
import com.eshop.MenuService.Model.MenuItem;
import com.eshop.MenuService.Repository.MenuItemRepository;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class OrderStatusChangedToAwaitingStockValidationIntegrationEventHandler implements IIntegrationEventHandler<OrderStatusChangedToAwaitingStockValidationIntegrationEvent> {

    private final MenuItemRepository menuItemRepository;
    private final IEventBus eventBus;

    @Override
    public void handle(OrderStatusChangedToAwaitingStockValidationIntegrationEvent event) {
        log.info("⏳ OrderStatusChangedToAwaitingStockValidationIntegrationEvent received for OrderId: {}", event.getOrderId());
        List<ConfirmedOrderStockItem> confirmedOrderStockItems = new ArrayList<>();
        boolean isStockSufficient = true;

        for(OrderStockItem orderStockItem : event.getOrderStockItems()) {
            MenuItem menuItemOptional = menuItemRepository.findById(orderStockItem.getProductId()).orElseThrow(()-> new ResourceNotFoundException("MenuItem", "ProductId", orderStockItem.getProductId().toString()));

            boolean hasStock = menuItemOptional.getAvailableStock() > orderStockItem.getUnits();
            if(!hasStock) {
                log.error("❌ Not enough stock for ProductId: {}", orderStockItem.getProductId());
                isStockSufficient = false;
            }
            confirmedOrderStockItems.add(new ConfirmedOrderStockItem(orderStockItem.getProductId(), hasStock));
        }

        IntegrationEvent resultEvent;
        if (isStockSufficient) {
            resultEvent = new OrderStockConfirmedIntegrationEvent(event.getOrderId());
            log.info("✅ All stock is available for OrderId: {}. Publishing OrderStockConfirmedIntegrationEvent.", event.getOrderId());
        } else {
            resultEvent = new OrderStockRejectedIntegrationEvent(event.getOrderId(), confirmedOrderStockItems);
            log.error("❌ Stock validation failed for OrderId: {}. Publishing OrderStockRejectedIntegrationEvent.", event.getOrderId());
        }

        eventBus.publish(resultEvent);

    }
}
