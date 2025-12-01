package com.eshop.OrderingService.Controller;

import com.eshop.OrderingService.DTO.CreateOrderFromBasketRequestDto;
import com.eshop.OrderingService.DTO.OrderDto;
import com.eshop.OrderingService.DTO.ResponseDto;
import com.eshop.OrderingService.Service.IOrderingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@AllArgsConstructor
public class OrderController {

    private final IOrderingService orderingService;

    @GetMapping("/my-orders")
    public ResponseEntity<ResponseDto<List<OrderDto>>> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(ResponseDto.success(orderingService.getOrdersByUserId(userId)));
    }

    @GetMapping("/my-orders/{orderId}")
    public ResponseEntity<ResponseDto<OrderDto>> getMyOrderDetail(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {

        OrderDto order = orderingService.getOrderByOrderId(orderId);
        String userId = jwt.getSubject();

        if (!order.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseDto.error("Bạn không có quyền xem đơn hàng này"));
        }

        return ResponseEntity.ok(ResponseDto.success(order));
    }

    @PostMapping("/create-from-basket")
    public ResponseEntity<Long> createOrderFromBasket(@RequestBody CreateOrderFromBasketRequestDto request) {
        log.info("Creating order from basket for user: {}", request.getUserId());
        Long orderId = orderingService.createOrderFromBasket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ResponseDto<Boolean>> cancelOrder(@PathVariable Long orderId,
                                                            @RequestParam(required = false) String reason,
                                                            @AuthenticationPrincipal Jwt jwt) {
        log.info("Cancelling order: {} with reason: {}", orderId, reason);
        String userId = jwt.getSubject();
        OrderDto order = orderingService.getOrderByOrderId(orderId);
        if (!order.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseDto.error("Bạn không được hủy đơn hàng của người khác"));
        }
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
