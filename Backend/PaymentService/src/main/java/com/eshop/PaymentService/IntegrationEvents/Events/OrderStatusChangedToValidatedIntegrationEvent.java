package com.eshop.PaymentService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderStatusChangedToValidatedIntegrationEvent extends IntegrationEvent {
    private UUID orderId;
    private BigDecimal Total;
}
