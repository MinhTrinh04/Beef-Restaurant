package com.eshop.MenuService.IntegrationEvents.Test;

import com.eshop.MenuService.IntegrationEvents.Events.OrderStatusChangedToAwaitingStockValidationIntegrationEvent;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lớp Controller này chỉ dùng cho mục đích kiểm thử (testing).
 * Nó cung cấp một endpoint để giả lập việc một service khác
 * (như OrderingService) phát hành sự kiện.
 */
@RestController
@RequestMapping("/api/v1/test-events")
@RequiredArgsConstructor
@Slf4j
public class EventTestController {

    private final IEventBus eventBus;

    /**
     * Endpoint để publish một sự kiện OrderStatusChangedToAwaitingStockValidationIntegrationEvent.
     *
     * @param event Dữ liệu của sự kiện được gửi trong body của HTTP POST request.
     * @return Một thông báo xác nhận.
     */
    @PostMapping("/publish/order-stock-validation")
    public ResponseEntity<String> publishOrderStockValidationEvent(
            @RequestBody OrderStatusChangedToAwaitingStockValidationIntegrationEvent event) {

        log.info("Received test request to publish OrderStatusChangedToAwaitingStockValidationIntegrationEvent for Order ID: {}", event.getOrderId());

        // Sử dụng event bus đã được inject để phát hành sự kiện
        eventBus.publish(event);

        return ResponseEntity.ok("Event for Order ID " + event.getOrderId() + " published successfully.");
    }
}