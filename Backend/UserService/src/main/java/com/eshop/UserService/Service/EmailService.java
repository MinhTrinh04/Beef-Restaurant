package com.eshop.UserService.Service;

import com.eshop.UserService.IntegrationEvents.Events.OrderCreatedForEmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from:noreply@beefrestaurant.com}")
    private String fromEmail;

    public void sendOrderCreatedWithPaymentLinkEmail(String toEmail, String customerName, Long orderId,
            Double totalAmount, String paymentUrl, List<OrderCreatedForEmailEvent.OrderItemInfo> orderItems) {
        // Ensure paymentUrl is not null
        if (paymentUrl == null || paymentUrl.isEmpty()) {
            paymentUrl = "#";
        }
        
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("📦 Đơn hàng #" + orderId + " đã được tạo - Vui lòng thanh toán");

            String htmlContent = buildOrderCreatedWithPaymentLinkEmailHtml(customerName, orderId, totalAmount,
                    paymentUrl, orderItems);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Order created with payment link email sent to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send order created with payment link email: {}", e.getMessage());
        }
    }

    public void sendOrderPaidEmail(String toEmail, String customerName, Long orderId,
            Double totalAmount, List<OrderCreatedForEmailEvent.OrderItemInfo> orderItems) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("✅ Đơn hàng #" + orderId + " thanh toán thành công");

            String htmlContent = buildOrderPaidEmailHtml(customerName, orderId, totalAmount, orderItems);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Order paid email sent to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send order paid email: {}", e.getMessage());
        }
    }

    public void sendOrderCancelledEmail(String toEmail, String customerName, Long orderId, String reason,
            Double totalAmount, String orderDetails) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("❌ Đơn hàng #" + orderId + " đã bị hủy");

            String htmlContent = buildOrderCancelledEmailHtml(customerName, orderId, reason, totalAmount, orderDetails);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Order cancellation email sent to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send order cancellation email: {}", e.getMessage());
        }
    }

    private String buildOrderCreatedWithPaymentLinkEmailHtml(String customerName, Long orderId, Double totalAmount,
            String paymentUrl, List<OrderCreatedForEmailEvent.OrderItemInfo> orderItems) {
        // Build order items table
        StringBuilder itemsHtml = new StringBuilder();
        itemsHtml.append("<table>");
        itemsHtml.append("<thead><tr><th>Sản phẩm</th><th>Số lượng</th><th>Giá</th><th>Thành tiền</th></tr></thead>");
        itemsHtml.append("<tbody>");

        for (OrderCreatedForEmailEvent.OrderItemInfo item : orderItems) {
            Double subtotal = item.getUnits() * item.getUnitPrice();
            itemsHtml.append("<tr>")
                    .append("<td>").append(item.getProductName()).append("</td>")
                    .append("<td>").append(item.getUnits()).append("</td>")
                    .append("<td>").append(formatCurrency(item.getUnitPrice())).append("</td>")
                    .append("<td>").append(formatCurrency(subtotal)).append("</td>")
                    .append("</tr>");
        }

        itemsHtml.append("</tbody>");
        itemsHtml.append("</table>");

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; }" +
                "    .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }"
                +
                "    .header { text-align: center; border-bottom: 3px solid #ff6b6b; padding-bottom: 20px; }" +
                "    .header h1 { color: #333; margin: 0; }" +
                "    .info-box { background: #e7f3ff; padding: 20px; margin: 20px 0; border-radius: 5px; border-left: 4px solid #0066cc; }"
                +
                "    .order-info { background: #f8f9fa; padding: 20px; margin: 20px 0; border-radius: 5px; }" +
                "    .order-info p { margin: 10px 0; }" +
                "    .total { font-size: 24px; color: #ff6b6b; font-weight: bold; }" +
                "    .btn { display: inline-block; background: #ff6b6b; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; font-weight: bold; }"
                +
                "    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; border-top: 1px solid #ddd; padding-top: 20px; }"
                +
                "    table { width: 100%; border-collapse: collapse; }" +
                "    th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }" +
                "    th { background: #f0f0f0; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h1>🍖 Beef Restaurant</h1>" +
                "      <p>Đơn hàng đã được tạo</p>" +
                "    </div>" +
                "    <p>Xin chào <strong>" + customerName + "</strong>,</p>" +
                "    <p>Cảm ơn bạn đã đặt hàng tại Beef Restaurant. Đơn hàng của bạn đã được tạo thành công.</p>" +
                "    <div class='info-box'>" +
                "      <p><strong>Mã đơn hàng:</strong> #" + orderId + "</p>" +
                "      <p><strong>Số tiền:</strong> <span class='total'>" + formatCurrency(totalAmount) + "</span></p>"
                +
                "      <p><strong>Trạng thái:</strong> ⏳ Chờ thanh toán</p>" +
                "    </div>" +
                "    <p><strong>Chi tiết đơn hàng:</strong></p>" +
                "    <div class='order-info'>" +
                itemsHtml.toString() +
                "    </div>" +
                "    <p style='text-align: center; margin: 30px 0;'>" +
                "      <a href='" + (paymentUrl != null ? paymentUrl : "#") + "' class='btn'>Thanh toán ngay</a>" +
                "    </p>" +
                "    <p style='color: #666; font-size: 14px;'>⏰ <strong>Lưu ý:</strong> Vui lòng hoàn tất thanh toán trong vòng 24 giờ. Nếu quá thời gian, đơn hàng sẽ bị hủy tự động.</p>"
                +
                "    <div class='footer'>" +
                "      <p>Beef Restaurant | Hotline: 1900-1234 | Email: support@beefrestaurant.com</p>" +
                "      <p>© 2025 Beef Restaurant. All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderPaidEmailHtml(String customerName, Long orderId, Double totalAmount,
            List<OrderCreatedForEmailEvent.OrderItemInfo> orderItems) {
        // Build order items table
        StringBuilder itemsHtml = new StringBuilder();
        itemsHtml.append("<table>");
        itemsHtml.append("<thead><tr><th>Sản phẩm</th><th>Số lượng</th><th>Giá</th><th>Thành tiền</th></tr></thead>");
        itemsHtml.append("<tbody>");

        for (OrderCreatedForEmailEvent.OrderItemInfo item : orderItems) {
            Double subtotal = item.getUnits() * item.getUnitPrice();
            itemsHtml.append("<tr>")
                    .append("<td>").append(item.getProductName()).append("</td>")
                    .append("<td>").append(item.getUnits()).append("</td>")
                    .append("<td>").append(formatCurrency(item.getUnitPrice())).append("</td>")
                    .append("<td>").append(formatCurrency(subtotal)).append("</td>")
                    .append("</tr>");
        }

        itemsHtml.append("</tbody>");
        itemsHtml.append("</table>");

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; }" +
                "    .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }"
                +
                "    .header { text-align: center; border-bottom: 3px solid #28a745; padding-bottom: 20px; }" +
                "    .header h1 { color: #333; margin: 0; }" +
                "    .success { color: #28a745; font-size: 18px; margin: 10px 0; font-weight: bold; }" +
                "    .info-box { background: #e8f5e9; padding: 20px; margin: 20px 0; border-radius: 5px; border-left: 4px solid #28a745; }"
                +
                "    .order-info { background: #f8f9fa; padding: 20px; margin: 20px 0; border-radius: 5px; }" +
                "    .order-info p { margin: 10px 0; }" +
                "    .total { font-size: 24px; color: #28a745; font-weight: bold; }" +
                "    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; border-top: 1px solid #ddd; padding-top: 20px; }"
                +
                "    table { width: 100%; border-collapse: collapse; }" +
                "    th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }" +
                "    th { background: #f0f0f0; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h1>🍖 Beef Restaurant</h1>" +
                "      <p class='success'>✅ Thanh toán thành công</p>" +
                "    </div>" +
                "    <p>Xin chào <strong>" + customerName + "</strong>,</p>" +
                "    <p>Cảm ơn bạn! Thanh toán của bạn đã được xác nhận thành công.</p>" +
                "    <div class='info-box'>" +
                "      <p><strong>Mã đơn hàng:</strong> #" + orderId + "</p>" +
                "      <p><strong>Số tiền thanh toán:</strong> <span class='total'>" + formatCurrency(totalAmount) + "</span></p>"
                +
                "      <p><strong>Trạng thái:</strong> ✅ Đã thanh toán</p>" +
                "    </div>" +
                "    <p><strong>Chi tiết đơn hàng:</strong></p>" +
                "    <div class='order-info'>" +
                itemsHtml.toString() +
                "    </div>" +
                "    <p style='color: #666; font-size: 14px;'>Đơn hàng của bạn đang được chuẩn bị. Chúng tôi sẽ liên hệ với bạn ngay khi hàng sẵn sàng giao.</p>"
                +
                "    <div class='footer'>" +
                "      <p>Beef Restaurant | Hotline: 1900-1234 | Email: support@beefrestaurant.com</p>" +
                "      <p>© 2025 Beef Restaurant. All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderCancelledEmailHtml(String customerName, Long orderId, String reason,
            Double totalAmount, String orderDetails) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; }" +
                "    .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }"
                +
                "    .header { text-align: center; border-bottom: 3px solid #dc3545; padding-bottom: 20px; }" +
                "    .header h1 { color: #333; margin: 0; }" +
                "    .error { color: #dc3545; font-size: 18px; margin: 10px 0; font-weight: bold; }" +
                "    .info-box { background: #ffe7e7; padding: 20px; margin: 20px 0; border-radius: 5px; border-left: 4px solid #dc3545; }"
                +
                "    .reason-box { background: #fff3cd; padding: 15px; margin: 15px 0; border-radius: 5px; border-left: 4px solid #ffc107; }"
                +
                "    .order-info { background: #f8f9fa; padding: 20px; margin: 20px 0; border-radius: 5px; }" +
                "    .total { font-size: 24px; color: #dc3545; font-weight: bold; }" +
                "    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; border-top: 1px solid #ddd; padding-top: 20px; }"
                +
                "    table { width: 100%; border-collapse: collapse; }" +
                "    th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }" +
                "    th { background: #f0f0f0; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h1>🍖 Beef Restaurant</h1>" +
                "      <p class='error'>❌ Đơn hàng bị hủy</p>" +
                "    </div>" +
                "    <p>Xin chào <strong>" + customerName + "</strong>,</p>" +
                "    <p>Chúng tôi thông báo rằng đơn hàng của bạn đã bị hủy.</p>" +
                "    <div class='info-box'>" +
                "      <p><strong>Mã đơn hàng:</strong> #" + orderId + "</p>" +
                "      <p><strong>Số tiền:</strong> <span class='total'>" + formatCurrency(totalAmount) + "</span></p>"
                +
                "      <p><strong>Trạng thái:</strong> ❌ Đã hủy</p>" +
                "    </div>" +
                "    <p><strong>Lý do hủy:</strong></p>" +
                "    <div class='reason-box'>" +
                "      " + (reason != null ? reason : "Không có lý do được cung cấp") +
                "    </div>" +
                "    <p><strong>Chi tiết đơn hàng:</strong></p>" +
                "    <div class='order-info'>" +
                orderDetails +
                "    </div>" +
                "    <p style='color: #666; font-size: 14px;'>Nếu đây là lỗi hoặc bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi ngay lập tức.</p>"
                +
                "    <div class='footer'>" +
                "      <p>Beef Restaurant | Hotline: 1900-1234 | Email: support@beefrestaurant.com</p>" +
                "      <p>© 2025 Beef Restaurant. All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    private String formatCurrency(Double amount) {
        return String.format("₫%,.0f", amount);
    }
}
