package com.eshop.BasketService.Exception;

public class BasketNotFoundException extends RuntimeException {
    public BasketNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with the given input data %s : '%s'", resourceName, fieldName, fieldValue));
    }}
