package com.eshop.OrderingService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * Event from OrderingService to UserService when order is created
 * Contains full order details enriched from database with payment link
 */
@Getter
@AllArgsConstructor
public class OrderCreatedForEmailEvent extends IntegrationEvent {
    private Long orderId;
    private String userEmail;
    private String userName;
    private Double totalAmount;
    private String paymentUrl;
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
