package com.eshop.OrderingService.IntegrationEvents.Events;

import lombok.Data;

@Data
public class OrderStockItem {
    private String productId;
    private Integer units;

    public OrderStockItem() {
    }

    public OrderStockItem(String productId, Integer units) {
        this.productId = productId;
        this.units = units;
    }
}
