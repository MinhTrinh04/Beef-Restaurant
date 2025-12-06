package com.eshop.UserService.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.from:noreply@beefrestaurant.com}")
    private String fromEmail;

    /**
     * Gửi email hóa đơn sau khi thanh toán thành công
     */
    public void sendOrderConfirmationEmail(String toEmail, String customerName, Long orderId,
            Double totalAmount, String qrCodeUrl, String orderDetails) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("✓ Xác nhận đơn hàng #" + orderId + " - Beef Restaurant");

            // Tạo HTML content
            String htmlContent = buildOrderEmailHtml(customerName, orderId, totalAmount, qrCodeUrl, orderDetails);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Order confirmation email sent successfully to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send order confirmation email to {}: {}", toEmail, e.getMessage());
            // Không throw exception - log warning để không ảnh hưởng main flow
        } catch (Exception e) {
            log.error("Unexpected error sending email: {}", e.getMessage());
        }
    }

    /**
     * Gửi email thông báo lỗi thanh toán
     */
    public void sendPaymentFailureEmail(String toEmail, String customerName, Long orderId, String reason) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("✗ Thanh toán thất bại - Đơn hàng #" + orderId);

            String htmlContent = buildPaymentFailureEmailHtml(customerName, orderId, reason);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Payment failure email sent to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send payment failure email: {}", e.getMessage());
        }
    }

    /**
     * Gửi email xác nhận đơn hàng đã được tạo (trước khi thanh toán)
     */
    public void sendOrderCreatedEmail(String toEmail, String customerName, Long orderId, Double totalAmount) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Đơn hàng #" + orderId + " đã được tạo - Vui lòng thanh toán");

            String htmlContent = buildOrderCreatedEmailHtml(customerName, orderId, totalAmount);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Order created email sent to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send order created email: {}", e.getMessage());
        }
    }

    /**
     * Build HTML content cho email xác nhận đơn hàng
     */
    private String buildOrderEmailHtml(String customerName, Long orderId, Double totalAmount,
            String qrCodeUrl, String orderDetails) {
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
                "    .success { color: #28a745; font-size: 18px; margin: 10px 0; }" +
                "    .order-info { background: #f8f9fa; padding: 20px; margin: 20px 0; border-radius: 5px; }" +
                "    .order-info p { margin: 10px 0; }" +
                "    .qr-code { text-align: center; margin: 30px 0; }" +
                "    .qr-code img { max-width: 250px; border: 1px solid #ddd; padding: 10px; }" +
                "    .total { font-size: 24px; color: #ff6b6b; font-weight: bold; }" +
                "    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; border-top: 1px solid #ddd; padding-top: 20px; }"
                +
                "    .btn { display: inline-block; background: #ff6b6b; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }"
                +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h1>🍖 Beef Restaurant</h1>" +
                "      <p class='success'>✓ Thanh toán thành công!</p>" +
                "    </div>" +
                "    <p>Xin chào <strong>" + customerName + "</strong>,</p>" +
                "    <p>Cảm ơn bạn đã đặt hàng tại Beef Restaurant. Dưới đây là chi tiết đơn hàng của bạn:</p>" +
                "    <div class='order-info'>" +
                "      <p><strong>Mã đơn hàng:</strong> #" + orderId + "</p>" +
                "      <p><strong>Tổng tiền:</strong> <span class='total'>" + formatCurrency(totalAmount)
                + "</span></p>" +
                "      <p><strong>Trạng thái:</strong> ✓ Đã thanh toán</p>" +
                "    </div>" +
                "    <div class='qr-code'>" +
                "      <p>Mã QR thanh toán:</p>" +
                "      <img src='" + qrCodeUrl + "' alt='QR Code'>" +
                "    </div>" +
                "    <p>Chi tiết đơn hàng:</p>" +
                "    <div class='order-info'>" +
                orderDetails +
                "    </div>" +
                "    <p>Đơn hàng của bạn sẽ được chuẩn bị và giao trong thời gian sớm nhất.</p>" +
                "    <p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi.</p>" +
                "    <div class='footer'>" +
                "      <p>Beef Restaurant | Hotline: 1900-1234 | Email: support@beefrestaurant.com</p>" +
                "      <p>© 2025 Beef Restaurant. All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build HTML content cho email thông báo lỗi thanh toán
     */
    private String buildPaymentFailureEmailHtml(String customerName, Long orderId, String reason) {
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
                "    .error { color: #dc3545; font-size: 18px; margin: 10px 0; }" +
                "    .info { background: #fff3cd; padding: 20px; margin: 20px 0; border-radius: 5px; border-left: 4px solid #ffc107; }"
                +
                "    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; border-top: 1px solid #ddd; padding-top: 20px; }"
                +
                "    .btn { display: inline-block; background: #ff6b6b; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }"
                +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h1>🍖 Beef Restaurant</h1>" +
                "      <p class='error'>✗ Thanh toán thất bại</p>" +
                "    </div>" +
                "    <p>Xin chào <strong>" + customerName + "</strong>,</p>" +
                "    <p>Đơn hàng #" + orderId + " của bạn không thể được hoàn tất vì lỗi thanh toán:</p>" +
                "    <div class='info'>" +
                "      <strong>Lý do:</strong> " + reason +
                "    </div>" +
                "    <p>Vui lòng thử lại hoặc liên hệ với bộ phận hỗ trợ để được trợ giúp.</p>" +
                "    <a href='http://localhost:3000/orders' class='btn'>Quay lại đặt hàng</a>" +
                "    <div class='footer'>" +
                "      <p>Beef Restaurant | Hotline: 1900-1234 | Email: support@beefrestaurant.com</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build HTML content cho email thông báo đơn hàng được tạo
     */
    private String buildOrderCreatedEmailHtml(String customerName, Long orderId, Double totalAmount) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; }" +
                "    .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 8px; }"
                +
                "    .header { text-align: center; border-bottom: 3px solid #ff6b6b; padding-bottom: 20px; }" +
                "    .header h1 { color: #333; margin: 0; }" +
                "    .info { background: #e7f3ff; padding: 20px; margin: 20px 0; border-radius: 5px; }" +
                "    .total { font-size: 22px; color: #ff6b6b; font-weight: bold; }" +
                "    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h1>🍖 Beef Restaurant</h1>" +
                "      <p>Đơn hàng đã được tạo</p>" +
                "    </div>" +
                "    <p>Xin chào <strong>" + customerName + "</strong>,</p>" +
                "    <p>Đơn hàng của bạn đã được tạo thành công. Vui lòng hoàn tất thanh toán:</p>" +
                "    <div class='info'>" +
                "      <p><strong>Mã đơn hàng:</strong> #" + orderId + "</p>" +
                "      <p><strong>Số tiền cần thanh toán:</strong> <span class='total'>" + formatCurrency(totalAmount)
                + "</span></p>" +
                "    </div>" +
                "    <p>Nhấp vào nút dưới để tiếp tục thanh toán:</p>" +
                "    <a href='http://localhost:3000/payment/" + orderId
                + "' style='display:inline-block; background:#ff6b6b; color:white; padding:12px 30px; text-decoration:none; border-radius:5px;'>Thanh toán ngay</a>"
                +
                "    <div class='footer'>" +
                "      <p>© 2025 Beef Restaurant</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Format tiền tệ
     */
    private String formatCurrency(Double amount) {
        return String.format("₫%,.0f", amount);
    }
}
