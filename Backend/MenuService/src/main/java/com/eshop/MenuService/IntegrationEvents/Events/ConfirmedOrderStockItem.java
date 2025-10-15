package com.eshop.MenuService.IntegrationEvents.Events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConfirmedOrderStockItem {
    private Integer productId;
    private boolean hasStock;
}
