package com.eshop.BasketService.Exception;

public class StockValidationException extends RuntimeException {
    public StockValidationException(String message) {
        super(message);
    }
}