//package com.eshop.UserService.IntegrationEvents.EventHandling;
//
//import com.eshop.UserService.IntegrationEvents.Events.OrderPaymentSucceededIntegrationEvent;
//import com.eshop.UserService.Service.EmailService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class OrderPaymentSucceededIntegrationEventHandler {
//
//    @Autowired
//    private EmailService emailService;
//
//    @RabbitListener(queues = "order.payment.succeeded")
//    public void handleOrderPaymentSucceeded(OrderPaymentSucceededIntegrationEvent event) {
//        try {
//            log.info("Received OrderPaymentSucceededEvent for orderId: {}, email: {}",
//                    event.getOrderId(), event.getEmail());
//
//            // Gửi email xác nhận đơn hàng cho khách
//            emailService.sendOrderConfirmationEmail(
//                    event.getEmail(),
//                    event.getCustomerName(),
//                    event.getOrderId(),
//                    event.getTotalAmount(),
//                    event.getQrCodeUrl(),
//                    event.getOrderDetails());
//
//            log.info("Order confirmation email sent successfully for orderId: {}", event.getOrderId());
//
//        } catch (Exception e) {
//            log.error("Error handling OrderPaymentSucceededEvent: {}", e.getMessage(), e);
//            // Có thể implement retry logic ở đây
//        }
//    }
//}
