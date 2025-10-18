package com.eshop.OrderingService.IntegrationEvents.Events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStockItem {

    private Integer productId;
    private String productName;
    private Integer units;
    private String pictureUrl;
}
