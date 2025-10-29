package com.eshop.BasketService.Model;

import lombok.Data;
import java.math.BigDecimal;


@Data
public class BasketItem {

    private Integer productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer units;
    private String pictureUrl;
}