package com.eshop.BasketService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockValidationItem {
    private Integer menuItemId;
    private Integer units;
}
