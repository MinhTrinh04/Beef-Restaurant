package com.eshop.OrderingService.Config;

import com.fasterxml.jackson.databind.JavaType; // <-- Import kiểu trả về ĐÚNG
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * Ánh xạ (map) một class name đầy đủ (ví dụ: com.basket...)
 * sang một class name cục bộ (ví dụ: com.ordering...)
 * bằng cách khớp tên class đơn giản.
 *
 * Phiên bản này đã được sửa để GHI ĐÈ ĐÚNG phương thức của Spring AMQP 3.x
 * (Sửa cả kiểu trả về và tham số).
 */
public class ConventionBasedJavaTypeMapper extends DefaultJackson2JavaTypeMapper {

    private final String localEventsPackage;

    public ConventionBasedJavaTypeMapper(String localEventsPackage) {
        super();
        this.localEventsPackage = localEventsPackage.endsWith(".")
                ? localEventsPackage
                : localEventsPackage + ".";
    }

    /**
     * Đây là phương thức GHI ĐÈ ĐÚNG.
     * Kiểu trả về là: JavaType
     * Tham số là: MessageProperties
     */
    @Override
    @NonNull
    public JavaType toJavaType(@NonNull MessageProperties properties) {
        // Lấy headers từ MessageProperties
        Map<String, Object> headers = properties.getHeaders();
        String originalClassId = (String) headers.get(getClassIdFieldName());

        if (originalClassId == null) {
            return super.toJavaType(properties);
        }

        try {
            // Tách lấy tên class đơn giản
            String simpleName = originalClassId.substring(originalClassId.lastIndexOf('.') + 1);

            // Xây dựng tên class cục bộ mới
            String localClassId = this.localEventsPackage + simpleName;

            // QUAN TRỌNG: Sửa đổi header __TypeId__ TRỰC TIẾP
            // trước khi gọi superclass.
            headers.put(getClassIdFieldName(), localClassId);

            // Để superclass (với header đã được sửa đổi) thực hiện
            // việc load class và xác thực tin cậy (trusted packages).
            // Lần này, kiểu trả về JavaType sẽ khớp.
            return super.toJavaType(properties);

        } catch (Exception e) {
            // Quay lại hành vi mặc định nếu logic của chúng ta thất bại
            return super.toJavaType(properties);
        }
    }
}