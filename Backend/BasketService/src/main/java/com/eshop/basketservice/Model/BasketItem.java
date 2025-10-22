package com.eshop.basketservice.Model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BasketItem implements Serializable {
    private Integer productId;
    private String productName;
    private BigDecimal unitPrice;
    @Min(value = 1, message = "Số lượng không hợp lệ")
    private int quantity;
    private String pictureUrl;
}