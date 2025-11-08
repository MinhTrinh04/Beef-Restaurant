package com.eshop.MenuService.Constants;

public class MenuConstants {
    private MenuConstants() {

    }
    public static final String QUEUE_NAME = "menu-service-queue";
    public static final String ORDER_STATUS_CHANGE_TO_PAID_INTEGRATION_EVENT = "OrderStatusChangedToPaidIntegrationEvent";
    public static final String ORDER_STATUS_CHANGE_TO_AWAITING_STOCK_VALIDATION_INTEGRATION_EVENT = "OrderStatusChangedToAwaitingStockValidationIntegrationEvent";
    public static final String ORDER_STATUS_CHANGE_TO_PAID_INTEGRATION_EVENT_V2 = "OrderStatusChangedToPaidIntegrationEventV2";
    public static final String ORDER_STATUS_CHANGE_TO_CANCELLED_INTEGRATION_EVENT = "OrderStatusChangedToCancelledIntegrationEvent";
    public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "Account created successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again or contact Dev team";


}
