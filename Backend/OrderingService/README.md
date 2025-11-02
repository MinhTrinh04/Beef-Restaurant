# Ordering Service

Ordering Service là một microservice xử lý logic đặt hàng trong hệ thống nhà hàng Beef Restaurant. Service này được xây dựng dựa trên sơ đồ tuần tự (Order SEQ) và tuân thủ kiến trúc của MenuService.

## Kiến trúc

Service được tổ chức theo cấu trúc layered architecture:

```
OrderingService/
├── Constants/           # Các hằng số của service
├── Controller/          # API endpoints
├── DTO/                # Data Transfer Objects
├── Exception/          # Custom exceptions
├── IntegrationEvents/  # Integration events và handlers
│   ├── EventHandling/  # Event handlers
│   ├── Events/         # Event definitions
│   └── Test/           # Test controllers
├── Mapper/             # Entity-DTO mappers
├── Model/              # Database entities
├── Repository/         # Data access layer
└── Service/            # Business logic
```

## Luồng xử lý đơn hàng

### 1. Tạo đơn hàng từ checkout

-   **Event**: `UserCheckoutAcceptedIntegrationEvent`
-   **Handler**: `UserCheckoutAcceptedIntegrationEventHandler`
-   **Action**: Tạo đơn hàng mới với trạng thái `Submitted`

### 2. Xác thực kho hàng

-   **Event**: `OrderStatusChangedToAwaitingStockValidationIntegrationEvent`
-   **Handler**: MenuService xử lý
-   **Action**: Kiểm tra tồn kho sản phẩm

### 3. Xác nhận thanh toán

-   **Event**: `OrderPaymentSucceededIntegrationEvent` / `OrderPaymentFailedIntegrationEvent`
-   **Handler**: `OrderPaymentSucceededIntegrationEventHandler` / `OrderPaymentFailedIntegrationEventHandler`
-   **Action**: Cập nhật trạng thái đơn hàng

## API Endpoints

### Order Management

-   `POST /api/v1/orders` - Tạo đơn hàng mới
-   `GET /api/v1/orders/{id}` - Lấy đơn hàng theo ID
-   `GET /api/v1/orders/order-id/{orderId}` - Lấy đơn hàng theo Order ID
-   `GET /api/v1/orders/user/{userId}` - Lấy đơn hàng theo User ID
-   `GET /api/v1/orders` - Lấy tất cả đơn hàng
-   `PUT /api/v1/orders/{orderId}/cancel` - Hủy đơn hàng
-   `PUT /api/v1/orders/{orderId}/ship` - Gửi đơn hàng

### Integration Events Testing

-   `POST /api/v1/integration-events/test/user-checkout-accepted` - Test checkout event
-   `POST /api/v1/integration-events/test/order-stock-confirmed/{orderId}` - Test stock confirmed
-   `POST /api/v1/integration-events/test/order-stock-rejected/{orderId}` - Test stock rejected
-   `POST /api/v1/integration-events/test/order-payment-succeeded/{orderId}` - Test payment success
-   `POST /api/v1/integration-events/test/order-payment-failed/{orderId}` - Test payment failure
-   `POST /api/v1/integration-events/test/full-order-flow` - Test toàn bộ luồng

## Trạng thái đơn hàng

-   `Submitted` - Đơn hàng đã được tạo
-   `AwaitingStockValidation` - Đang chờ xác thực kho
-   `Validated` - Đã xác thực kho, chờ thanh toán
-   `Paid` - Đã thanh toán
-   `Shipped` - Đã gửi hàng
-   `Cancelled` - Đã hủy

## Integration Events

### Outgoing Events (OrderingService publishes)

-   `OrderStatusChangedToSubmittedIntegrationEvent`
-   `OrderStatusChangedToAwaitingStockValidationIntegrationEvent`
-   `OrderStatusChangedToValidatedIntegrationEvent`
-   `OrderStatusChangedToPaidIntegrationEvent`
-   `OrderStatusChangedToShippedIntegrationEvent`
-   `OrderStatusChangedToCancelledIntegrationEvent`

### Incoming Events (OrderingService handles)

-   `UserCheckoutAcceptedIntegrationEvent`
-   `OrderStockConfirmedIntegrationEvent`
-   `OrderStockRejectedIntegrationEvent`
-   `OrderPaymentSucceededIntegrationEvent`
-   `OrderPaymentFailedIntegrationEvent`

## Cấu hình

### Database

-   H2 in-memory database cho development
-   Flyway migration cho schema management
-   JPA/Hibernate cho ORM

### Message Queue

-   RabbitMQ cho integration events
-   JSON message serialization
-   Topic exchanges với routing keys

### Port

-   Service chạy trên port 8082

## Chạy service

```bash
cd OrderingService
./mvnw spring-boot:run
```

## Testing

Service cung cấp các endpoint test để kiểm tra integration events:

1. Test checkout: `POST /api/v1/integration-events/test/user-checkout-accepted`
2. Test full flow: `POST /api/v1/integration-events/test/full-order-flow`

## Dependencies

-   Spring Boot 3.5.6
-   Spring Data JPA
-   Spring AMQP (RabbitMQ)
-   H2 Database
-   Flyway
-   Lombok
-   BuildingBlocks (custom event bus)
