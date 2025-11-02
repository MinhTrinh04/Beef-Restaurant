package com.eshop.MenuService.IntegrationEvents.EventHandling;

import com.eshop.MenuService.Exception.ResourceNotFoundException;
import com.eshop.MenuService.IntegrationEvents.Events.OrderStatusChangedToCancelledIntegrationEvent;
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
public class OrderStatusChangedToCancelledIntegrationEventHandler implements IIntegrationEventHandler<OrderStatusChangedToCancelledIntegrationEvent> {

    private final MenuItemRepository menuItemRepository;

    @Override
    @Transactional
    public void handle(OrderStatusChangedToCancelledIntegrationEvent event) {
        log.info("⏳ OrderStatusChangedToCancelledIntegrationEvent received for OrderId: {}", event.getOrderId());

        for (OrderStockItem orderStockItem : event.getOrderItems()) {
            Optional<MenuItem> menuItemOptional = menuItemRepository.findById(orderStockItem.getProductId());

            if (menuItemOptional.isPresent()) {
                MenuItem menuItem = menuItemOptional.get();
                log.info("✅ Updating stock for MenuItem ID: {}. Old stock: {}, Old reserved stock: {} ",
                        menuItem.getId(), menuItem.getAvailableStock(), menuItem.getReservedStock());
                menuItem.setAvailableStock(menuItem.getAvailableStock() + orderStockItem.getUnits());
                menuItem.setReservedStock(menuItem.getReservedStock() - orderStockItem.getUnits());
                log.info("✅ Updating stock for MenuItem ID: {}.  New stock: {}, New reserved stock: {}",
                        menuItem.getId(), menuItem.getAvailableStock(), menuItem.getReservedStock());
            } else {
                throw new ResourceNotFoundException("MenuItem", "ProductId", orderStockItem.getProductId().toString());
            }
        }

    }

}
