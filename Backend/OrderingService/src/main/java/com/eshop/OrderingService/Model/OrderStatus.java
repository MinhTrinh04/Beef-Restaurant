package com.eshop.OrderingService.Model;

public enum OrderStatus {
    Submitted,
    AwaitingStockValidation,
    Validated,
    Paid,
    Shipped,
    Completed,
    Cancelled
}
