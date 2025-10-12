-- Create orders table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    buyer_id VARCHAR(255) NOT NULL,
    order_date TIMESTAMP NOT NULL,
    order_status VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    street VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    payment_method VARCHAR(100),
    total DECIMAL(10,2)
);

-- Create order_items table
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    units INTEGER NOT NULL,
    picture_url VARCHAR(500),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_orders_buyer_id ON orders(buyer_id);
CREATE INDEX idx_orders_order_status ON orders(order_status);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);
