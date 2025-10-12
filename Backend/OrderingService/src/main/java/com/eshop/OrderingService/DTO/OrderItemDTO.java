package com.eshop.OrderingService.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private String productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer units;
    private String pictureUrl;
}
