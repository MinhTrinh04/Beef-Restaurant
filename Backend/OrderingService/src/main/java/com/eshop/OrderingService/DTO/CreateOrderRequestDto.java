package com.eshop.OrderingService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    private String userId;
    private String description;

    // Address fields
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String addressCountry;

    // Buyer information
    private String buyerName;
    private String buyerEmail;

    private List<OrderItemRequestDto> orderItems;
}
