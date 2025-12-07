package com.eshop.UserService.IntegrationEvents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * Email event from OrderingService to UserService when order is created
 * Contains order details with payment link for email sending
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
