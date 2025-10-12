package com.eshop.OrderingService.DTO;

import com.eshop.OrderingService.Model.OrderStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String buyerId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private String description;
    private AddressDTO address;
    private String paymentMethod;
    private List<OrderItemDTO> orderItems;
    private Double total;
}
