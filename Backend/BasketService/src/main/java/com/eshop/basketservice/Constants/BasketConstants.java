package com.eshop.basketservice.Constants;

public class BasketConstants {
    private BasketConstants() {

    }
    public static final String QUEUE_NAME = "basket-service-queue";
    public static final String  ORDER_STATUS_CHANGE_TO_SUBMITTED = "OrderStatusChangedToSubmitted";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again or contact Dev team";


}
