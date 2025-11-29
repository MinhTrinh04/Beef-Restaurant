package com.eshop.OrderingService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long id;
    private Long orderId;
    private Integer productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer units;
    private String pictureUrl;
}
