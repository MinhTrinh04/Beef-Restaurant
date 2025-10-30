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
public class OrderStatusChangedToAwaitingStockValidationIntegrationEventV2Handler implements IIntegrationEventHandler<OrderStatusChangedToAwaitingStockValidationIntegrationEventV2> {

    private final MenuItemRepository menuItemRepository;
    private final IEventBus eventBus;

    @Override
    public void handle(OrderStatusChangedToAwaitingStockValidationIntegrationEventV2 event) {
        log.info("⏳ OrderStatusChangedToAwaitingStockValidationIntegrationEventV2 received for OrderId: {}", event.getOrderId());
        List<ConfirmedOrderStockItemV2> confirmedOrderStockItems = new ArrayList<>();
        boolean isStockSufficient = true;

        for(OrderStockItem orderStockItem : event.getOrderStockItems()) {
            MenuItem menuItemOptional = menuItemRepository.findById(orderStockItem.getProductId()).orElseThrow(()-> new ResourceNotFoundException("MenuItem", "ProductId", orderStockItem.getProductId().toString()));

            boolean hasStock = menuItemOptional.getAvailableStock() > orderStockItem.getUnits();
            if(!hasStock) {
                log.error("❌ Not enough stock for ProductId: {}", orderStockItem.getProductId());
                isStockSufficient = false;
            }
            confirmedOrderStockItems.add(new ConfirmedOrderStockItemV2(orderStockItem.getProductId(), orderStockItem.getUnits()));
        }

        IntegrationEvent resultEvent;
        if (isStockSufficient) {
            confirmedOrderStockItems.forEach(item -> {
                Optional<MenuItem> menuItemOptional = menuItemRepository.findById(item.getProductId());
                if (menuItemOptional.isPresent()) {
                    MenuItem menuItem = menuItemOptional.get();
                    // Giảm tồn kho
                    menuItem.Removestock(item.getUnits());
                    log.info("✅ Updating stock for MenuItem ID: {}. Old stock: {}, New stock: {}",
                            menuItem.getId(), menuItemOptional.get().getAvailableStock(), menuItem.getAvailableStock());
                    // Tạm giữ
                    menuItem.setReservedStock(menuItem.getReservedStock() + item.getUnits());
                    log.info("✅ Updating reserved stock for MenuItem ID: {}. Old stock: {}, New stock: {}",
                            menuItem.getId(), menuItemOptional.get().getReservedStock(), menuItem.getReservedStock());
                    menuItemRepository.save(menuItem);
                }
            });
            resultEvent = new OrderStockConfirmedIntegrationEvent(event.getOrderId());
            log.info("✅ All stock is available for OrderId: {}. Publishing OrderStockConfirmedIntegrationEvent.", event.getOrderId());
        } else {
            resultEvent = new OrderStockRejectedIntegrationEventV2(event.getOrderId(), confirmedOrderStockItems);
            log.error("❌ Stock validation failed for OrderId: {}. Publishing OrderStockRejectedIntegrationEventV2.", event.getOrderId());
        }

        eventBus.publish(resultEvent);

    }
}
