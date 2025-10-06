package com.eshop.MenuService.IntergrationEvents.Events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedOrderStockItem {
    private String productId;
    private boolean hasStock;
}
