package com.eshop.OrderingService.Job;

import com.eshop.OrderingService.IntegrationEvents.Events.OrderStatusChangedToCancelledIntegrationEvent;
import com.eshop.OrderingService.IntegrationEvents.Events.OrderStockItem;
import com.eshop.OrderingService.Model.Order;
import com.eshop.OrderingService.Repository.OrderRepository;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.eshop.OrderingService.Constants.OrderingConstants.ORDER_STATUS_VALIDATED;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderTimeoutJob {

    private final OrderRepository orderRepository;
    private final IEventBus eventBus;

    @Value("${app.payment.timeout-minutes}")
    private long paymentTimeoutMinutes;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void cancelPendingOrders() {
        log.info("JOB: Bắt đầu quét các đơn hàng (status={}) quá hạn...", ORDER_STATUS_VALIDATED);

        LocalDateTime timeoutThreshold = LocalDateTime.now().minusMinutes(paymentTimeoutMinutes);

        List<Order> stuckOrders = orderRepository.findByOrderStatusAndUpdatedAtBefore(
                ORDER_STATUS_VALIDATED,
                timeoutThreshold
        );

        if (stuckOrders.isEmpty()) {
            log.info("JOB: Không tìm thấy đơn hàng nào quá hạn.");
            return;
        }

        log.warn("JOB: Tìm thấy {} đơn hàng quá hạn. Bắt đầu hủy...", stuckOrders.size());

        for (Order order : stuckOrders) {

            // 3. Cập nhật trạng thái
            order.setOrderStatus("Cancelled");
            orderRepository.save(order);
            log.info("JOB: Đã hủy đơn hàng do hết hạn thanh toán: {}", order.getOrderId());


            List<OrderStockItem> stockItems = order.getOrderItems().stream()
                    .map(item -> new OrderStockItem(item.getProductId(), item.getProductName(), item.getUnits(), item.getPictureUrl()))
                    .collect(Collectors.toList());

            eventBus.publish(new OrderStatusChangedToCancelledIntegrationEvent(
                    order.getOrderId(),
                    order.getBuyerId(),
                    "Hết thời gian thanh toán",
                    stockItems
            ));
        }

        log.info("JOB: Hoàn tất quét.");
    }
}
