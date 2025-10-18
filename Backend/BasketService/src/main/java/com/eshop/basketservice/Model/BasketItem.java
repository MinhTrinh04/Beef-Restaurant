package com.eshop.basketservice.Model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BasketItem {
    private UUID id;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private String pictureUrl;
}