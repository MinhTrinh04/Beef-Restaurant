package com.eshop.MenuService.IntergrationEvents.Events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class OrderStockItem {
    private String productId;
    private int units;
}
