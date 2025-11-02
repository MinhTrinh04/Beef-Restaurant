package com.eshop.OrderingService.Exception;

public class InvalidOrderStatusException extends RuntimeException {

    public InvalidOrderStatusException(String message) {
        super(message);
    }

    public InvalidOrderStatusException(String currentStatus, String requestedAction) {
        super(String.format("Cannot perform '%s' on order with status '%s'", requestedAction, currentStatus));
    }
}
