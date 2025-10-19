package com.eshop.OrderingService.Controller;

import com.eshop.OrderingService.IntegrationEvents.Events.*;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/integration-events")
@Slf4j
@AllArgsConstructor
public class IntegrationEventController {

    private final IEventBus eventBus;

    @PostMapping("/test/user-checkout-accepted")
    public ResponseEntity<String> testUserCheckoutAccepted() {
        log.info("Testing UserCheckoutAcceptedIntegrationEvent");

        List<BasketItem> basketItems = Arrays.asList(
                new BasketItem(1, "Beef Steak", 2, "https://example.com/beef-steak.jpg"),
                new BasketItem(2, "Beef Burger", 1, "https://example.com/beef-burger.jpg"));

        UserCheckoutAcceptedIntegrationEvent event = new UserCheckoutAcceptedIntegrationEvent(
                "user-123",
                "John Doe",
                "Ho Chi Minh City",
                "123 Main Street",
                "Ho Chi Minh",
                "Vietnam",
                "700000",
                "1234567890123456",
                "John Doe",
                "12/25",
                "123",
                1,
                "John Doe",
                "john.doe@example.com",
                basketItems);

        eventBus.publish(event);

        return ResponseEntity.ok("UserCheckoutAcceptedIntegrationEvent published successfully");
    }

    @PostMapping("/test/order-stock-confirmed/{orderId}")
    public ResponseEntity<String> testOrderStockConfirmed(@PathVariable String orderId) {
        log.info("Testing OrderStockConfirmedIntegrationEvent for order: {}", orderId);

        OrderStockConfirmedIntegrationEvent event = new OrderStockConfirmedIntegrationEvent(orderId);
        eventBus.publish(event);

        return ResponseEntity.ok("OrderStockConfirmedIntegrationEvent published successfully");
    }

    @PostMapping("/test/order-stock-rejected/{orderId}")
    public ResponseEntity<String> testOrderStockRejected(@PathVariable String orderId) {
        log.info("Testing OrderStockRejectedIntegrationEvent for order: {}", orderId);

        List<ConfirmedOrderStockItem> confirmedItems = Arrays.asList(
                new ConfirmedOrderStockItem(1, false),
                new ConfirmedOrderStockItem(2, true));

        OrderStockRejectedIntegrationEvent event = new OrderStockRejectedIntegrationEvent(orderId, confirmedItems);
        eventBus.publish(event);

        return ResponseEntity.ok("OrderStockRejectedIntegrationEvent published successfully");
    }

    @PostMapping("/test/order-payment-succeeded/{orderId}")
    public ResponseEntity<String> testOrderPaymentSucceeded(@PathVariable String orderId) {
        log.info("Testing OrderPaymentSucceededIntegrationEvent for order: {}", orderId);

        OrderPaymentSucceededIntegrationEvent event = new OrderPaymentSucceededIntegrationEvent(orderId);
        eventBus.publish(event);

        return ResponseEntity.ok("OrderPaymentSucceededIntegrationEvent published successfully");
    }

    @PostMapping("/test/order-payment-failed/{orderId}")
    public ResponseEntity<String> testOrderPaymentFailed(@PathVariable String orderId) {
        log.info("Testing OrderPaymentFailedIntegrationEvent for order: {}", orderId);

        OrderPaymentFailedIntegrationEvent event = new OrderPaymentFailedIntegrationEvent(orderId,
                "Insufficient funds");
        eventBus.publish(event);

        return ResponseEntity.ok("OrderPaymentFailedIntegrationEvent published successfully");
    }
}
