package com.eshop.MenuService.IntegrationEvents.Events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConfirmedOrderStockItemV2 {
    private Integer productId;
    private Integer units;
}
