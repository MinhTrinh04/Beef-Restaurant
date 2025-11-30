package com.eshop.PaymentService.Service;

import com.eshop.PaymentService.DTO.CreatePaymentUrlRequestDto;
import com.eshop.PaymentService.IntegrationEvents.Events.OrderPaymentFailedIntegrationEvent;
import com.eshop.PaymentService.IntegrationEvents.Events.OrderPaymentSucceededIntegrationEvent;
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
        // Lưu ý: PayOS giới hạn độ dài description
        String description = "Thanh toan don " + orderCode;
        if (description.length() > 25) description = description.substring(0, 25);

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

        log.info("PayOS Checkout URL created for Order {}: {}", orderCode, response.getCheckoutUrl());
        return response.getCheckoutUrl();
    }

    /**
     * Xử lý Webhook từ PayOS (Thay thế cho VNPay Callback)
     */
    public void handleWebhook(Object webhookBody) {
        try {
            // 1. Xác thực Webhook (Verify Signature)
            // PayOS SDK có hàm verify, nhưng nó cần ObjectNode hoặc String JSON
            // Để đơn giản, ta convert Object nhận được về đúng kiểu
            WebhookData webhookData = payOS.webhooks().verify(webhookBody);

            Long orderId = webhookData.getOrderCode();
            String desc = webhookData.getDescription();

            // 2. Kiểm tra trạng thái thanh toán
            // Nếu webhook được gửi đến, thường nghĩa là thanh toán thành công (hoặc hủy)
            // PayOS quy định code "00" là thành công
            if ("00".equals(webhookData.getCode())) {
                log.info("✅ PayOS Webhook: Payment SUCCESS for OrderId: {}", orderId);

                // Bắn sự kiện "Thanh toán thành công" -> OrderingService sẽ nghe thấy
                OrderPaymentSucceededIntegrationEvent successEvent = new OrderPaymentSucceededIntegrationEvent(orderId);
                eventBus.publish(successEvent);
            } else {
                log.warn("❌ PayOS Webhook: Payment FAILED/CANCELLED for OrderId: {}. Desc: {}", orderId, desc);

                // Bắn sự kiện "Thanh toán thất bại"
                OrderPaymentFailedIntegrationEvent failedEvent = new OrderPaymentFailedIntegrationEvent(orderId, desc);
                eventBus.publish(failedEvent);
            }

        } catch (Exception e) {
            log.error("Error processing PayOS Webhook: ", e);
            // Có thể throw exception để PayOS biết và retry nếu cần
            throw new RuntimeException("Webhook verification failed");
        }
    }
}