package com.eshop.MenuService.IntegrationEvents.Events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderStockItem {
    private Integer productId;
    private Integer units;
}
