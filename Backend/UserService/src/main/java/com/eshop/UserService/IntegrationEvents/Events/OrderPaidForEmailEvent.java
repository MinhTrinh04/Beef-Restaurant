package com.eshop.UserService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * Event from OrderingService to UserService when payment is successful
 * Contains full order details enriched from database
 */
@Getter
@AllArgsConstructor
public class OrderPaidForEmailEvent extends IntegrationEvent {
    private Long orderId;
    private String userEmail;
    private String userName;
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
