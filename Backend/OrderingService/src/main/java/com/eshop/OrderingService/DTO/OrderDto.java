package com.eshop.OrderingService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private UUID orderId;
    private String userId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private String description;

    // Address fields
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String addressCountry;

    // Buyer information
    private String buyerName;
    private String buyerEmail;
    private BigDecimal totalAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<OrderItemDto> orderItems;
}
