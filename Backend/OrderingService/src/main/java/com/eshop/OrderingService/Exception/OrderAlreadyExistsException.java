package com.eshop.OrderingService.Exception;

public class OrderAlreadyExistsException extends RuntimeException {

    public OrderAlreadyExistsException(String message) {
        super(message);
    }

    public OrderAlreadyExistsException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
