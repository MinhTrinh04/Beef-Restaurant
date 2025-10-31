package com.eshop.PaymentService.Service;

import com.eshop.PaymentService.Config.VNPayConfig;
import com.eshop.PaymentService.DTO.CreatePaymentUrlRequestDto;
import com.eshop.PaymentService.DTO.VNPayCallbackResponseDto;
import com.eshop.PaymentService.IntegrationEvents.Events.OrderPaymentFailedIntegrationEvent;
import com.eshop.PaymentService.IntegrationEvents.Events.OrderPaymentSucceededIntegrationEvent;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class VNPayService {

    private final IEventBus eventBus;

    public String createPaymentUrl(CreatePaymentUrlRequestDto dto, HttpServletRequest req) throws UnsupportedEncodingException {
        long amount = dto.getAmount().longValue() * 100; // VNPay yêu cầu đơn vị xu
        String vnp_TxnRef = dto.getOrderId().toString();
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (dto.getBankCode() != null && !dto.getBankCode().isEmpty()) {
            vnp_Params.put("vnp_BankCode", dto.getBankCode());
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other"); // Loại hàng hóa: other

        String locate = dto.getLanguage();
        if (locate == null || locate.isEmpty()) {
            locate = "vn";
        }
        vnp_Params.put("vnp_Locale", locate);

        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl); // URL VNPay gọi lại sau khi thanh toán xong
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15); // Hết hạn sau 15 phút
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    public VNPayCallbackResponseDto handleVNPayCallback(HttpServletRequest request) {
        try {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHash");

            List<String> fieldNames = new ArrayList<>(fields.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = fields.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLDecoder.decode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }


            String calculatedHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());

            log.info("Received VNPay Callback. Hash: {}, Calculated Hash: {}", vnp_SecureHash, calculatedHash);
            log.info("Received VNPay Callback Data: {}", fields);



            String orderIdStr = request.getParameter("vnp_TxnRef");
            UUID orderId = null;
            try {
                orderId = UUID.fromString(orderIdStr);
            } catch (IllegalArgumentException e) {
                log.error("VNPay callback failed: Invalid OrderId format. Received: {}", orderIdStr);
                return new VNPayCallbackResponseDto("02", "Order Not Found"); // Mã 02: Đơn hàng không tồn tại
            }

            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus"); // Trạng thái giao dịch chi tiết


            if ("00".equals(vnp_ResponseCode) && "00".equals(vnp_TransactionStatus)) {
                log.info("✅ VNPay payment successful for OrderId: {}", orderId);
                OrderPaymentSucceededIntegrationEvent successEvent = new OrderPaymentSucceededIntegrationEvent(orderId);
                eventBus.publish(successEvent);
                return new VNPayCallbackResponseDto("00", "Confirm Success"); // Mã 00: Thành công
            } else {
                log.warn("❌ VNPay payment failed or cancelled for OrderId: {}. ResponseCode: {}, TransactionStatus: {}", orderId, vnp_ResponseCode, vnp_TransactionStatus);
                OrderPaymentFailedIntegrationEvent failedEvent = new OrderPaymentFailedIntegrationEvent(orderId);
                eventBus.publish(failedEvent);
                return new VNPayCallbackResponseDto("99", "Payment Failed");
            }

        } catch (Exception e) {
            log.error("Error processing VNPay callback", e);
            return new VNPayCallbackResponseDto("99", "Unknown error"); // Mã 99: Lỗi không xác định
        }
    }
}