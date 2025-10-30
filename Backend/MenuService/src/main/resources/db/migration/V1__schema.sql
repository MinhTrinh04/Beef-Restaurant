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
    reserved_stock  INT              NOT NULL DEFAULT 0,
    menu_category_id INT              NOT NULL,
    CONSTRAINT fk_menu_items_to_categories FOREIGN KEY (menu_category_id) REFERENCES menu_categories (id)
);

-- Chèn dữ liệu cho bảng danh mục (menu_categories)
INSERT INTO menu_categories (name, slug, description)
VALUES ('Khai vị', 'khai-vi', 'Những món ăn nhẹ nhàng để bắt đầu bữa tiệc.'),
       ('Bít tết hảo hạng', 'bit-tet-hao-hang', 'Những phần thịt bò thượng hạng được chế biến công phu.'),
       ('Món chính khác', 'mon-chinh-khac', 'Các lựa chọn món chính đa dạng ngoài bít tết.'),
       ('Món ăn kèm', 'mon-an-kem', 'Các món ăn phụ để hoàn thiện bữa ăn của bạn.'),
       ('Tráng miệng', 'trang-mieng', 'Những món ngọt ngào để kết thúc bữa ăn.'),
       ('Đồ uống', 'do-uong', 'Danh sách rượu vang, cocktail và các loại đồ uống khác.');

-- Chèn dữ liệu cho bảng món ăn (menu_items)

-- Món khai vị (category_id = 1)
INSERT INTO menu_items (name, description, price, slug, image, available_stock, menu_category_id)
VALUES ('Súp bí đỏ kem nấm truffle', 'Súp bí đỏ sánh mịn với hương thơm đặc trưng của dầu nấm truffle.', 120000,
        'sup-bi-do-truffle', 'images/starters/pumpkin-soup.jpg', 50, 1),
       ('Salad Caesar với tôm nướng', 'Salad rau romaine tươi giòn, bánh mì nướng, phô mai Parmesan và tôm nướng.',
        180000, 'salad-caesar-tom', 'images/starters/caesar-salad.jpg', 40, 1),
       ('Hàu nướng phô mai', 'Hàu tươi nướng với phô mai mozzarella và sốt bơ tỏi.', 250000, 'hau-nuong-pho-mai',
        'images/starters/baked-oysters.jpg', 30, 1);

-- Bít tết hảo hạng (category_id = 2)
INSERT INTO menu_items (name, description, price, slug, image, available_stock, menu_category_id)
VALUES ('Bò Wagyu A5 Nhật Bản', 'Thăn ngoại bò Wagyu A5 trứ danh từ Nhật Bản, mềm tan trong miệng.', 2500000,
        'bo-wagyu-a5', 'images/steaks/wagyu-a5.jpg', 15, 2),
       ('Thăn ngoại bò Black Angus', 'Thăn ngoại bò Black Angus Mỹ cao cấp, mọng nước và đậm đà hương vị.', 750000,
        'than-ngoai-black-angus', 'images/steaks/black-angus-striploin.jpg', 30, 2),
       ('Thăn nội bò (Tenderloin)', 'Phần thịt mềm nhất của con bò, được phục vụ với sốt tiêu xanh.', 850000,
        'than-noi-bo', 'images/steaks/tenderloin.jpg', 25, 2),
       ('T-Bone Steak', 'Sự kết hợp hoàn hảo giữa thăn ngoại và thăn nội trong cùng một phần bít tết.', 950000,
        't-bone-steak', 'images/steaks/t-bone.jpg', 20, 2);

-- Món chính khác (category_id = 3)
INSERT INTO menu_items (name, description, price, slug, image, available_stock, menu_category_id)
VALUES ('Cá hồi áp chảo sốt chanh dây', 'Cá hồi Na-uy áp chảo da giòn, ăn kèm sốt chanh dây chua ngọt.', 450000,
        'ca-hoi-ap-chao', 'images/mains/salmon.jpg', 35, 3),
       ('Sườn cừu nướng thảo mộc', 'Sườn cừu nướng với hương thảo và các loại thảo mộc, ăn kèm khoai tây nghiền.',
        650000, 'suon-cuu-nuong', 'images/mains/lamb-chops.jpg', 25, 3);

-- Món ăn kèm (category_id = 4)
INSERT INTO menu_items (name, description, price, slug, image, available_stock, menu_category_id)
VALUES ('Khoai tây nghiền', 'Khoai tây nghiền mịn với bơ và kem.', 80000, 'khoai-tay-nghien',
        'images/sides/mashed-potatoes.jpg', 100, 4),
       ('Rau củ nướng', 'Các loại rau củ theo mùa được nướng với dầu oliu và thảo mộc.', 95000, 'rau-cu-nuong',
        'images/sides/roasted-vegetables.jpg', 100, 4),
       ('Măng tây xào tỏi', 'Măng tây tươi xanh xào nhanh với tỏi và dầu oliu.', 110000, 'mang-tay-xao-toi',
        'images/sides/asparagus.jpg', 100, 4);

-- Tráng miệng (category_id = 5)
INSERT INTO menu_items (name, description, price, slug, image, available_stock, menu_category_id)
VALUES ('Tiramisu', 'Bánh Tiramisu truyền thống của Ý với hương cà phê và rượu rum.', 150000, 'tiramisu',
        'images/desserts/tiramisu.jpg', 40, 5),
       ('Chocolate Lava Cake', 'Bánh sô cô la với nhân sô cô la nóng chảy, ăn kèm kem vani.', 160000,
        'chocolate-lava-cake', 'images/desserts/lava-cake.jpg', 40, 5);

-- Đồ uống (category_id = 6)
INSERT INTO menu_items (name, description, price, slug, image, available_stock, menu_category_id)
VALUES ('Rượu vang đỏ Cabernet Sauvignon', 'Một chai rượu vang đỏ đậm đà, lý tưởng để dùng chung với bít tết.', 1200000,
        'vang-do-cabernet', 'images/drinks/red-wine.jpg', 50, 6),
       ('Nước suối khoáng', 'Nước khoáng thiên nhiên đóng chai.', 50000, 'nuoc-suoi', 'images/drinks/water.jpg', 100,
        6);