package com.eshop.OrderingService.Constants;

public class OrderingConstants {

    // Order Status Constants
    public static final String ORDER_STATUS_SUBMITTED = "Submitted";
    public static final String ORDER_STATUS_AWAITING_STOCK_VALIDATION = "AwaitingStockValidation";
    public static final String ORDER_STATUS_VALIDATED = "Validated";
    public static final String ORDER_STATUS_PAID = "Paid";
    public static final String ORDER_STATUS_SHIPPED = "Shipped";
    public static final String ORDER_STATUS_CANCELLED = "Cancelled";

    // Event Names
    public static final String USER_CHECKOUT_ACCEPTED_EVENT = "UserCheckoutAcceptedIntegrationEvent";
    public static final String USER_CHECKOUT_ACCEPTED_EVENT_V2 = "UserCheckoutAcceptedIntegrationEventV2";
    public static final String ORDER_STATUS_CHANGED_TO_SUBMITTED_EVENT = "OrderStatusChangedToSubmittedIntegrationEvent";
    public static final String ORDER_STATUS_CHANGED_TO_AWAITING_STOCK_VALIDATION_EVENT = "OrderStatusChangedToAwaitingStockValidationIntegrationEvent";
    public static final String ORDER_STATUS_CHANGED_TO_VALIDATED_EVENT = "OrderStatusChangedToValidatedIntegrationEvent";
    public static final String ORDER_STATUS_CHANGED_TO_PAID_EVENT = "OrderStatusChangedToPaidIntegrationEvent";
    public static final String ORDER_STATUS_CHANGED_TO_SHIPPED_EVENT = "OrderStatusChangedToShippedIntegrationEvent";
    public static final String ORDER_STATUS_CHANGED_TO_CANCELLED_EVENT = "OrderStatusChangedToCancelledIntegrationEvent";
    public static final String ORDER_STOCK_CONFIRMED_EVENT = "OrderStockConfirmedIntegrationEvent";
    public static final String ORDER_STOCK_REJECTED_EVENT = "OrderStockRejectedIntegrationEvent";
    public static final String ORDER_PAYMENT_SUCCEEDED_EVENT = "OrderPaymentSucceededIntegrationEvent";
    public static final String ORDER_PAYMENT_FAILED_EVENT = "OrderPaymentFailedIntegrationEvent";

    // Grace Period (in seconds)
    public static final int GRACE_PERIOD_SECONDS = 15;

    // Default values
    public static final String DEFAULT_CARD_TYPE = "Visa";
    public static final String DEFAULT_CURRENCY = "VND";



    public static final String QUEUE_NAME = "order-service-queue";
}
