package com.eshop.OrderingService.Mapper;

import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.DTO.OrderItemDto;
import com.eshop.OrderingService.Model.Order;
import com.eshop.OrderingService.Model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setDescription(order.getDescription());

        // Address fields
        dto.setAddressStreet(order.getAddressStreet());
        dto.setAddressCity(order.getAddressCity());
        dto.setAddressState(order.getAddressState());
        dto.setAddressCountry(order.getAddressCountry());
        dto.setAddressZipCode(order.getAddressZipCode());

        // Payment fields
        dto.setCardNumber(order.getCardNumber());
        dto.setCardHolderName(order.getCardHolderName());
        dto.setCardExpiration(order.getCardExpiration());
        dto.setCardSecurityNumber(order.getCardSecurityNumber());
        dto.setCardTypeId(order.getCardTypeId());

        // Buyer information
        dto.setBuyerName(order.getBuyerName());
        dto.setBuyerEmail(order.getBuyerEmail());
        dto.setTotalAmount(order.getTotalAmount());

        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        // Map order items
        if (order.getOrderItems() != null) {
            List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            dto.setOrderItems(orderItemDtos);
        }

        return dto;
    }

    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setOrderId(orderItem.getOrder().getId());
        dto.setProductId(orderItem.getProductId());
        dto.setProductName(orderItem.getProductName());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setUnits(orderItem.getUnits());
        dto.setPictureUrl(orderItem.getPictureUrl());

        return dto;
    }
}
