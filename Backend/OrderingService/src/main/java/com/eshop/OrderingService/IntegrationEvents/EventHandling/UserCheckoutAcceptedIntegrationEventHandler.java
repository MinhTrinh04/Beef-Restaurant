package com.eshop.OrderingService.IntegrationEvents.EventHandling;

import com.eshop.OrderingService.DTO.AddressDTO;
import com.eshop.OrderingService.DTO.OrderDTO;
import com.eshop.OrderingService.DTO.OrderItemDTO;
import com.eshop.OrderingService.Service.IOrderingService;
import com.eshop.OrderingService.IntegrationEvents.Events.UserCheckoutAcceptedIntegrationEvent;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserCheckoutAcceptedIntegrationEventHandler
        implements IIntegrationEventHandler<UserCheckoutAcceptedIntegrationEvent> {

    private final IOrderingService orderingService;

    @Override
    public void handle(UserCheckoutAcceptedIntegrationEvent event) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerId(event.getUserId());

        // Map address
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(event.getStreet());
        addressDTO.setCity(event.getCity());
        addressDTO.setCountry(event.getCountry());
        orderDTO.setAddress(addressDTO);

        // Map payment method
        orderDTO.setPaymentMethod(event.getCardTypeId());

        // Map order items
        List<OrderItemDTO> orderItems = event.getBasketItems().stream()
                .map(basketItem -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setProductId(basketItem.getProductId());
                    orderItemDTO.setProductName(basketItem.getProductName());
                    orderItemDTO.setUnitPrice(basketItem.getUnitPrice());
                    orderItemDTO.setUnits(basketItem.getQuantity());
                    orderItemDTO.setPictureUrl(basketItem.getPictureUrl());
                    return orderItemDTO;
                })
                .collect(Collectors.toList());
        orderDTO.setOrderItems(orderItems);

        // Calculate total
        Double total = orderItems.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getUnits())
                .sum();
        orderDTO.setTotal(total);

        orderingService.createOrder(orderDTO);
    }
}
