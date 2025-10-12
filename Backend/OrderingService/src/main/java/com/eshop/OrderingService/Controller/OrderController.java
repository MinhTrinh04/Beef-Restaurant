package com.eshop.OrderingService.Controller;

import com.eshop.OrderingService.DTO.OrderDTO;
import com.eshop.OrderingService.Service.IOrderingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderingService orderingService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam String buyerId) {
        return ResponseEntity.ok(orderingService.getOrdersByBuyerId(buyerId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderingService.getOrderById(orderId));
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestBody Long orderId) {
        orderingService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderDTO orderDTO) {
        orderingService.createOrder(orderDTO);
        return ResponseEntity.ok().build();
    }
}
