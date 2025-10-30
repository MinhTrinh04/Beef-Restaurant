-- Create Orders table
CREATE TABLE orders
(
    order_id        UUID PRIMARY KEY,
    buyer_id        VARCHAR(36)    NOT NULL,
    order_date      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    order_status    VARCHAR(50)    NOT NULL DEFAULT 'Submitted',
    description     VARCHAR(500),
    address_street  VARCHAR(200),
    address_city    VARCHAR(100),
    address_state   VARCHAR(100),
    address_country VARCHAR(100),
    buyer_name      VARCHAR(200),
    buyer_email     VARCHAR(200),
    total_amount    DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    created_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    payment_url     VARCHAR(1024)
);

-- Create Order Items table
CREATE TABLE order_items
(
    id                BIGSERIAL PRIMARY KEY,
    product_id        BIGINT NOT NULL,
    product_name      VARCHAR(255),
    unit_price        DECIMAL(19, 2),
    units             INT,
    picture_url       VARCHAR(255),
    order_id          UUID,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE
);