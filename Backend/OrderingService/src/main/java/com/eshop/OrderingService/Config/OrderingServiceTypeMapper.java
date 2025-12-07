package com.eshop.OrderingService.Config;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * Custom type mapper to support events from multiple services
 * Automatically resolves event classes from OrderingService and PaymentService
 * packages
 */
public class OrderingServiceTypeMapper extends DefaultJackson2JavaTypeMapper {

    private final String[] eventPackages = {
            "com.eshop.OrderingService.IntegrationEvents.Events",
            "com.eshop.PaymentService.IntegrationEvents.Events"
    };

    @Override
    @NonNull
    public JavaType toJavaType(@NonNull MessageProperties properties) {
        Map<String, Object> headers = properties.getHeaders();
        String originalClassId = (String) headers.get(getClassIdFieldName());

        if (originalClassId == null) {
            return super.toJavaType(properties);
        }

        try {
            // Extract simple class name
            String simpleName = originalClassId.substring(originalClassId.lastIndexOf('.') + 1);

            // Try to find the class in any of the supported packages
            for (String pkg : eventPackages) {
                String localClassId = pkg + "." + simpleName;
                try {
                    headers.put(getClassIdFieldName(), localClassId);
                    return super.toJavaType(properties);
                } catch (Exception e) {
                    // Try next package
                }
            }

            // If not found in any package, use original
            headers.put(getClassIdFieldName(), originalClassId);
            return super.toJavaType(properties);

        } catch (Exception e) {
            return super.toJavaType(properties);
        }
    }
}
