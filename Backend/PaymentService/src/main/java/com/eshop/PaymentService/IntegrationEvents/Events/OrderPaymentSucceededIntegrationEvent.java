package com.eshop.PaymentService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentSucceededIntegrationEvent extends IntegrationEvent {
    private Long orderId;
    private String userEmail;
    private String userName;
    private BigDecimal totalAmount;
    private List<OrderItemInfo> orderItems;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemInfo {
        private String productName;
        private Integer units;
        private BigDecimal unitPrice;
        private String pictureUrl;
    }
}
