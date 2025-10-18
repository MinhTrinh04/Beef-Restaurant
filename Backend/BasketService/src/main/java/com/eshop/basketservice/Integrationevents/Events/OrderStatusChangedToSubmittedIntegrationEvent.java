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

    /**
     * ID của đơn hàng vừa được tạo.
     */
    private UUID orderId;

    /**
     * ID của người mua hàng, dùng để xác định và xóa giỏ hàng.
     */
    private String buyerId;
}