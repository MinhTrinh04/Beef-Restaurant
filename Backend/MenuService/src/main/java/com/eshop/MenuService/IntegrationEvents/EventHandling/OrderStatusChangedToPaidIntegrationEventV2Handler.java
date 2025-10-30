package com.eshop.MenuService.IntegrationEvents.EventHandling;

import com.eshop.MenuService.Exception.ResourceNotFoundException;
import com.eshop.MenuService.IntegrationEvents.Events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshop.MenuService.IntegrationEvents.Events.OrderStockItem;
import com.eshop.MenuService.Model.MenuItem;
import com.eshop.MenuService.Repository.MenuItemRepository;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class OrderStatusChangedToPaidIntegrationEventV2Handler implements IIntegrationEventHandler<OrderStatusChangedToPaidIntegrationEvent> {

    private final MenuItemRepository menuItemRepository;

    @Override
    @Transactional
    public void handle(OrderStatusChangedToPaidIntegrationEvent event) {
        log.info("⏳ OrderStatusChangedToPaidIntegrationEventV2 received for OrderId: {}", event.getOrderId());

        List<MenuItem> itemsToUpdate = new ArrayList<>();

        for (OrderStockItem orderStockItem : event.getOrderStockItems()) {
            Optional<MenuItem> menuItemOptional = menuItemRepository.findById(orderStockItem.getProductId());

            if (menuItemOptional.isPresent()) {
                MenuItem menuItem = menuItemOptional.get();
                menuItem.setReservedStock(menuItem.getReservedStock() - orderStockItem.getUnits());
                log.info("✅ Updating reserved stock for MenuItem ID: {}. Old stock: {}, New stock: {}",
                        menuItem.getId(), menuItemOptional.get().getReservedStock(), menuItem.getReservedStock());
                itemsToUpdate.add(menuItem);
            } else {
                throw new ResourceNotFoundException("MenuItem", "ProductId", orderStockItem.getProductId().toString());
            }
        }

        if (!itemsToUpdate.isEmpty()) {
            menuItemRepository.saveAll(itemsToUpdate);
            log.info("✅Successfully updated stock for {} items for OrderId: {}.", itemsToUpdate.size(), event.getOrderId());
        } else {
            log.warn("❌ No items were updated for OrderId: {}.", event.getOrderId());
        }

    }

}
