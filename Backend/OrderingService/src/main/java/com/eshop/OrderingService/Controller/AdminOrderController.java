package com.eshop.OrderingService.Controller;

import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.DTO.ResponseDto;
import com.eshop.OrderingService.Service.IOrderingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/orders")
@Slf4j
@AllArgsConstructor
public class AdminOrderController {

    private final IOrderingService orderingService;

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

    @GetMapping("/{orderId}")
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
}