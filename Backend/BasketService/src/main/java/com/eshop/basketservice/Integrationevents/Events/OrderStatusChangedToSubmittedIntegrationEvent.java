package com.eshop.basketservice.Integrationevents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangedToSubmittedIntegrationEvent extends IntegrationEvent {

    private UUID orderId;
    private String orderStatus;
    private String buyerId;
}