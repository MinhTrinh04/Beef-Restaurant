package com.eshop.UserService.Constant;

public class UserConstants {
    private UserConstants() {
    }

    public static final String QUEUE_NAME = "user-service-queue";

    public static final String THANK_YOU_ORDER_EMAIL_EVENT = "ThankYouOrderEmailEvent";
    public static final String ORDER_CREATED_FOR_EMAIL_EVENT = "OrderCreatedForEmailEvent";
    public static final String ORDER_PAID_FOR_EMAIL_EVENT = "OrderPaidForEmailEvent";
    public static final String ORDER_CANCELLED_FOR_EMAIL_EVENT = "OrderCancelledForEmailEvent";
}
