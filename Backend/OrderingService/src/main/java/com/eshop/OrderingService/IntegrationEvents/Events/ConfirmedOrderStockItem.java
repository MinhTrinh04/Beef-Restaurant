package com.eshop.OrderingService.IntegrationEvents.Events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedOrderStockItem {

    private Integer productId;
    private boolean hasStock;
}
