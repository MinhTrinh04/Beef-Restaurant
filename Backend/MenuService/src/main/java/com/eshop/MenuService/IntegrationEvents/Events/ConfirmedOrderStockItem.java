package com.eshop.MenuService.IntegrationEvents.Events;

import lombok.Getter;

@Getter
public class ConfirmedOrderStockItem {
    private String productId;
    private boolean hasStock;
}
