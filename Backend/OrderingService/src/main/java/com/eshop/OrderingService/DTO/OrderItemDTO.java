package com.eshop.OrderingService.DTO;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private String productId;
    private String productName;
    private Double unitPrice;
    private Integer units;
    private String pictureUrl;
}
