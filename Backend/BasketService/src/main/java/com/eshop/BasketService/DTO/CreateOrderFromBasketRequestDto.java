package com.eshop.BasketService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderFromBasketRequestDto {
    private String userId;
    private String userEmail;
    private String city;
    private String street;
    private String state;
    private String country;
    private List<OrderItemRequestDto> orderItems;
    private BigDecimal totalAmount;
}

