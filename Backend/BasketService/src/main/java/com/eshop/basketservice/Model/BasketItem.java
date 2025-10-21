package com.eshop.basketservice.Model;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BasketItem implements Serializable {
    private UUID id;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    @Min(value = 1, message = "Số lượng không hợp lệ")
    private int quantity;
    private String pictureUrl;
}