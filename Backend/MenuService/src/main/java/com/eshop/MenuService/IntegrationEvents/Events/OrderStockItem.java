package com.eshop.MenuService.IntegrationEvents.Events;

import lombok.Getter;

@Getter
public class OrderStockItem {
    private String productId;
    private int units;
}
