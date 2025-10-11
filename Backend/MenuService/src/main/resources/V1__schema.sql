-- Tạo bảng cho các danh mục món ăn
CREATE TABLE menu_categories
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    slug        VARCHAR(150) UNIQUE,
    description VARCHAR(255)
);

-- Tạo bảng cho các món ăn
CREATE TABLE menu_items
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(100)     NOT NULL,
    description      TEXT,
    price            DOUBLE PRECISION NOT NULL,
    slug             VARCHAR(150) UNIQUE,
    image            VARCHAR(255),
    available_stock  INT              NOT NULL DEFAULT 0,
    menu_category_id INT              NOT NULL,
    CONSTRAINT fk_menu_items_to_categories FOREIGN KEY (menu_category_id) REFERENCES menu_categories (id)
);