package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderCancelledForEmailEvent extends IntegrationEvent {
    private Long orderId;
    private String userEmail;
    private String userName;
    private String reason;
    private Double totalAmount;
    private List<OrderItemInfo> orderItems;

    @Getter
    @AllArgsConstructor
    public static class OrderItemInfo {
        private String productName;
        private Integer units;
        private Double unitPrice;
        private String pictureUrl;
    }
}
