package com.eshop.basketservice.Model;

import lombok.Data;
import java.math.BigDecimal;


@Data
public class BasketItem {

    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private String pictureUrl;
}