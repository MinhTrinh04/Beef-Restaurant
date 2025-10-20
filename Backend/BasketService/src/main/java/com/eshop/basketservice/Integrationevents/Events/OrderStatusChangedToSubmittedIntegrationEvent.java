package com.eshop.basketservice.Integrationevents.Events;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Sự kiện được publish khi một đơn hàng được tạo thành công và có trạng thái "Submitted".
 * Basket Service sẽ lắng nghe sự kiện này để xóa giỏ hàng tương ứng khỏi Redis.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangedToSubmittedIntegrationEvent extends IntegrationEvent {

    private UUID orderId;
    private String orderStatus;
    private String buyerId;
}