package com.eshop.OrderingService.Controller;

import com.eshop.OrderingService.DTO.CreateOrderRequestDto;
import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.DTO.ResponseDto;
import com.eshop.OrderingService.Service.IOrderingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@AllArgsConstructor
public class OrderController {

    private final IOrderingService orderingService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderDto>> getOrderById(@PathVariable Long id) {
        log.info("Getting order by ID: {}", id);

        try {
            OrderDto order = orderingService.getOrderById(id);
            return ResponseEntity.ok(ResponseDto.success(order));
        } catch (Exception e) {
            log.error("Failed to get order by ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.error("Order not found: " + e.getMessage()));
        }
    }

    @GetMapping("/order-id/{orderId}")
    public ResponseEntity<ResponseDto<OrderDto>> getOrderByOrderId(@PathVariable UUID orderId) {
        log.info("Getting order by Order ID: {}", orderId);

        try {
            OrderDto order = orderingService.getOrderByOrderId(orderId);
            return ResponseEntity.ok(ResponseDto.success(order));
        } catch (Exception e) {
            log.error("Failed to get order by Order ID {}: {}", orderId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.error("Order not found: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDto<List<OrderDto>>> getOrdersByUserId(@PathVariable String userId) {
        log.info("Getting orders for user: {}", userId);

        try {
            List<OrderDto> orders = orderingService.getOrdersByUserId(userId);
            return ResponseEntity.ok(ResponseDto.success(orders));
        } catch (Exception e) {
            log.error("Failed to get orders for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.error("Failed to get orders: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<OrderDto>>> getAllOrders() {
        log.info("Getting all orders");

        try {
            List<OrderDto> orders = orderingService.getAllOrders();
            return ResponseEntity.ok(ResponseDto.success(orders));
        } catch (Exception e) {
            log.error("Failed to get all orders: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.error("Failed to get orders: " + e.getMessage()));
        }
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ResponseDto<Boolean>> cancelOrder(@PathVariable UUID orderId,
            @RequestParam(required = false) String reason) {
        log.info("Cancelling order: {} with reason: {}", orderId, reason);

        try {
            boolean cancelled = orderingService.cancelOrder(orderId,
                    reason != null ? reason : "User requested cancellation");
            return ResponseEntity.ok(ResponseDto.success("Order cancelled successfully", cancelled));
        } catch (Exception e) {
            log.error("Failed to cancel order {}: {}", orderId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.error("Failed to cancel order: " + e.getMessage()));
        }
    }

}
