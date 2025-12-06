CREATE TABLE eshop_users (
    email VARCHAR(255) PRIMARY KEY,
    keycloak_user_id VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_keycloak_user_id ON eshop_users(keycloak_user_id);
CREATE INDEX idx_created_at ON eshop_users(created_at);