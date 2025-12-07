package com.eshop.UserService.IntegrationEvents.EventHandling;

import com.eshop.UserService.Service.EmailService;
import com.eshop.UserService.IntegrationEvents.Events.OrderCancelledForEmailEvent;
import com.eshop.buildingblocks.EventBus.Abstractions.IIntegrationEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Handles OrderCancelledForEmailEvent from OrderingService
 * Sends order cancellation email notification to user
 */
@Service
@Slf4j
@AllArgsConstructor
public class OrderCancelledForEmailEventHandler implements IIntegrationEventHandler<OrderCancelledForEmailEvent> {

    private final EmailService emailService;

    @Override
    public void handle(OrderCancelledForEmailEvent event) {
        log.info("📧 OrderCancelledForEmailEvent received for OrderId: {}", event.getOrderId());

        try {
            // Build HTML table from order items
            StringBuilder orderDetailsHtml = new StringBuilder();
            orderDetailsHtml.append("<table style='width: 100%; border-collapse: collapse;'>");
            orderDetailsHtml.append("<tr style='background-color: #f0f0f0;'>");
            orderDetailsHtml
                    .append("<th style='border: 1px solid #ddd; padding: 10px; text-align: left;'>Sản phẩm</th>");
            orderDetailsHtml
                    .append("<th style='border: 1px solid #ddd; padding: 10px; text-align: center;'>Số lượng</th>");
            orderDetailsHtml
                    .append("<th style='border: 1px solid #ddd; padding: 10px; text-align: right;'>Giá đơn vị</th>");
            orderDetailsHtml
                    .append("<th style='border: 1px solid #ddd; padding: 10px; text-align: right;'>Thành tiền</th>");
            orderDetailsHtml.append("</tr>");

            for (OrderCancelledForEmailEvent.OrderItemInfo item : event.getOrderItems()) {
                double totalPrice = item.getUnitPrice() * item.getUnits();
                orderDetailsHtml.append("<tr>");
                orderDetailsHtml.append("<td style='border: 1px solid #ddd; padding: 10px;'>")
                        .append(item.getProductName()).append("</td>");
                orderDetailsHtml.append("<td style='border: 1px solid #ddd; padding: 10px; text-align: center;'>")
                        .append(item.getUnits()).append("</td>");
                orderDetailsHtml.append("<td style='border: 1px solid #ddd; padding: 10px; text-align: right;'>₫")
                        .append(String.format("%,.0f", item.getUnitPrice())).append("</td>");
                orderDetailsHtml.append("<td style='border: 1px solid #ddd; padding: 10px; text-align: right;'>₫")
                        .append(String.format("%,.0f", totalPrice)).append("</td>");
                orderDetailsHtml.append("</tr>");
            }

            orderDetailsHtml.append("</table>");

            // Send email using existing method
            emailService.sendOrderCancelledEmail(
                    event.getUserEmail(),
                    event.getUserName(),
                    event.getOrderId(),
                    event.getReason(),
                    event.getTotalAmount(),
                    orderDetailsHtml.toString());

            log.info("✅ Order cancelled email sent for OrderId: {}", event.getOrderId());

        } catch (Exception e) {
            log.error("❌ Failed to send order cancelled email for OrderId: {}. Error: {}", event.getOrderId(),
                    e.getMessage(), e);
        }
    }
}
