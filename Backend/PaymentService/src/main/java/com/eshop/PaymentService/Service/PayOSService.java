package com.eshop.PaymentService.Service;

import com.eshop.PaymentService.DTO.CreatePaymentUrlRequestDto;
import com.eshop.PaymentService.IntegrationEvents.Events.OrderPaymentCancelledIntegrationEvent;
import com.eshop.PaymentService.IntegrationEvents.Events.OrderPaymentFailedIntegrationEvent;
import com.eshop.PaymentService.IntegrationEvents.Events.OrderPaymentSucceededIntegrationEvent;
import com.eshop.PaymentService.IntegrationEvents.Events.OrderCreatedWithPaymentLinkNotificationEvent;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import vn.payos.model.webhooks.WebhookData;

@Service
@Slf4j
@RequiredArgsConstructor
public class PayOSService {

    private final PayOS payOS;
    private final IEventBus eventBus;
    private final ObjectMapper objectMapper; // Dùng để parse JSON webhook

    @Value("${payos.return-url}")
    private String returnUrl;

    @Value("${payos.cancel-url}")
    private String cancelUrl;

    public String createPaymentUrl(CreatePaymentUrlRequestDto requestDto) throws Exception {

        // 1. Lấy thông tin từ DTO (bây giờ orderId đã là Long rồi)
        Long orderCode = requestDto.getOrderId();
        Long amount = requestDto.getAmount().longValue(); // PayOS cần int/long

        // 2. Tạo nội dung thanh toán
        String description = "Thanh toan don " + orderCode;
        if (description.length() > 25)
            description = description.substring(0, 25);

        // 3. Tạo Item (bắt buộc)
        PaymentLinkItem item = PaymentLinkItem.builder()
                .name("Don hang #" + orderCode)
                .quantity(1)
                .price(amount)
                .build();

        // 4. Build Request
        CreatePaymentLinkRequest payOSRequest = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description(description)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .item(item)
                .build();

        // 5. Gọi SDK PayOS
        CreatePaymentLinkResponse response = payOS.paymentRequests().create(payOSRequest);

        String checkoutUrl = response.getCheckoutUrl();
        if (checkoutUrl == null || checkoutUrl.isEmpty()) {
            throw new RuntimeException("PayOS did not return checkout URL for Order: " + orderCode);
        }

        log.info("PayOS Checkout URL created for Order {}: {}", orderCode, checkoutUrl);

        // Publish order created with payment link event to OrderingService
        OrderCreatedWithPaymentLinkNotificationEvent event = new OrderCreatedWithPaymentLinkNotificationEvent(
                orderCode,
                checkoutUrl);
        eventBus.publish(event);
        log.info("✅ OrderCreatedWithPaymentLinkNotificationEvent published for OrderId: {}", orderCode);

        return checkoutUrl;
    }

    public void handleWebhook(Object webhookBody) {
        try {
            WebhookData webhookData = payOS.webhooks().verify(webhookBody);

            Long orderId = webhookData.getOrderCode();
            String desc = webhookData.getDescription();

            if ("00".equals(webhookData.getCode())) {
                log.info("✅ PayOS Webhook: Payment SUCCESS for OrderId: {}", orderId);

                // Publish payment succeeded event to OrderingService
                OrderPaymentSucceededIntegrationEvent succeededEvent = new OrderPaymentSucceededIntegrationEvent(
                        orderId,
                        null, // email - fetch from order
                        null, // userName - fetch from order
                        null, // totalAmount - fetch from order
                        null // orderItems - fetch from order
                );
                eventBus.publish(succeededEvent);
                log.info("✅ OrderPaymentSucceededIntegrationEvent published for OrderId: {}", orderId);
            } else {
                log.warn("❌ PayOS Webhook: Payment FAILED/CANCELLED for OrderId: {}. Desc: {}", orderId, desc);

                OrderPaymentFailedIntegrationEvent failedEvent = new OrderPaymentFailedIntegrationEvent(orderId, desc);
                eventBus.publish(failedEvent);
            }

        } catch (Exception e) {
            log.error("Error processing PayOS Webhook: ", e);
            throw new RuntimeException("Webhook verification failed");
        }
    }

    public void handlePaymentCancelled(Long orderId) {
        log.warn("🚫 Payment cancelled by user for OrderId: {}", orderId);

        OrderPaymentCancelledIntegrationEvent cancelledEvent = new OrderPaymentCancelledIntegrationEvent(
                orderId,
                "User cancelled payment");
        eventBus.publish(cancelledEvent);

        log.info("✅ OrderPaymentCancelledIntegrationEvent published for cancelled payment. OrderId: {}", orderId);
    }
}