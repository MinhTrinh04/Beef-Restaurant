package com.eshop.OrderingService.Mapper;

import com.eshop.OrderingService.DTO.AddressDTO;
import com.eshop.OrderingService.DTO.OrderDTO;
import com.eshop.OrderingService.DTO.OrderItemDTO;
import com.eshop.OrderingService.Model.Address;
import com.eshop.OrderingService.Model.Order;
import com.eshop.OrderingService.Model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO mapToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setBuyerId(order.getBuyerId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setDescription(order.getDescription());
        orderDTO.setAddress(mapToAddressDTO(order.getAddress()));
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setOrderItems(mapToOrderItemDTOs(order.getOrderItems()));
        orderDTO.setTotal(order.getTotal());
        return orderDTO;
    }

    public Order mapToOrder(OrderDTO orderDTO, Order order) {
        order.setBuyerId(orderDTO.getBuyerId());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setDescription(orderDTO.getDescription());
        order.setAddress(mapToAddress(orderDTO.getAddress()));
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setOrderItems(mapToOrderItems(orderDTO.getOrderItems()));
        order.setTotal(orderDTO.getTotal());
        return order;
    }

    private AddressDTO mapToAddressDTO(Address address) {
        if (address == null)
            return null;
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    private Address mapToAddress(AddressDTO addressDTO) {
        if (addressDTO == null)
            return null;
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        return address;
    }

    private List<OrderItemDTO> mapToOrderItemDTOs(List<OrderItem> orderItems) {
        if (orderItems == null)
            return null;
        return orderItems.stream()
                .map(this::mapToOrderItemDTO)
                .collect(Collectors.toList());
    }

    private List<OrderItem> mapToOrderItems(List<OrderItemDTO> orderItemDTOs) {
        if (orderItemDTOs == null)
            return null;
        return orderItemDTOs.stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toList());
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setProductId(orderItem.getProductId());
        orderItemDTO.setProductName(orderItem.getProductName());
        orderItemDTO.setUnitPrice(orderItem.getUnitPrice());
        orderItemDTO.setUnits(orderItem.getUnits());
        orderItemDTO.setPictureUrl(orderItem.getPictureUrl());
        return orderItemDTO;
    }

    private OrderItem mapToOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDTO.getId());
        orderItem.setProductId(orderItemDTO.getProductId());
        orderItem.setProductName(orderItemDTO.getProductName());
        orderItem.setUnitPrice(orderItemDTO.getUnitPrice());
        orderItem.setUnits(orderItemDTO.getUnits());
        orderItem.setPictureUrl(orderItemDTO.getPictureUrl());
        return orderItem;
    }
}
