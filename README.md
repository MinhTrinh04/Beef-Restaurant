# 🍖 Beef-Restaurant - Hệ Thống Nhà Hàng Microservices

Hệ thống quản lý nhà hàng được xây dựng với kiến trúc microservices, sử dụng Spring Boot, Next.js và React.

## 📋 Mục Lục

- [Tổng Quan Hệ Thống](#-tổng-quan-hệ-thống)
- [Yêu Cầu Hệ Thống](#-yêu-cầu-hệ-thống)
- [Kiến Trúc Dự Án](#-kiến-trúc-dự-án)
- [Hướng Dẫn Cài Đặt](#-hướng-dẫn-cài-đặt)
- [Cấu Hình Môi Trường](#-cấu-hình-môi-trường)
- [Chạy Dự Án](#-chạy-dự-án)
- [Truy Cập Ứng Dụng](#-truy-cập-ứng-dụng)
- [Xử Lý Sự Cố](#-xử-lý-sự-cố)

---

## 🎯 Tổng Quan Hệ Thống

Beef-Restaurant là một hệ thống quản lý nhà hàng hoàn chỉnh với:

- **Backend**: Kiến trúc microservices với Spring Boot
- **Frontend**: Ứng dụng web cho khách hàng (Next.js)
- **Admin Panel**: Trang quản trị (React + Vite)
- **Authentication**: Keycloak SSO
- **Message Broker**: RabbitMQ
- **Caching**: Redis
- **Database**: PostgreSQL, H2

---

## 💻 Yêu Cầu Hệ Thống

### Bắt Buộc

- **Java**: JDK 17 hoặc cao hơn
- **Maven**: 3.8+ (để build backend)
- **Node.js**: 18+ (khuyến nghị 20+)
- **pnpm**: 8+ (package manager cho Frontend)
- **npm**: 9+ (package manager cho Admin)
- **Docker**: 24+ và Docker Compose
- **Git**: Để clone repository

### Kiểm Tra Phiên Bản

```bash
# Kiểm tra Java
java -version

# Kiểm tra Maven
mvn -version

# Kiểm tra Node.js
node -v

# Kiểm tra pnpm
pnpm -v

# Kiểm tra npm
npm -v

# Kiểm tra Docker
docker --version
docker compose version
```

---

## 🏗️ Kiến Trúc Dự Án

```
Beef-Restaurant/
├── Backend/                    # Microservices Backend
│   ├── DiscoveryService/      # Eureka Server (Service Registry)
│   ├── GatewayService/        # API Gateway (Port 9000)
│   ├── UserService/           # Quản lý người dùng (Port 8084)
│   ├── MenuService/           # Quản lý thực đơn (Port 8080)
│   ├── BasketService/         # Quản lý giỏ hàng (Port 8081)
│   ├── OrderingService/       # Quản lý đơn hàng (Port 8082)
│   ├── PaymentService/        # Xử lý thanh toán (Port 8083)
│   ├── BuildingBlocks/        # Shared libraries
│   └── Docker-Compose/        # Docker configuration
│       ├── docker-compose.yml
│       └── common-config.yml
├── Frontend/                   # Next.js Customer App (Port 3000)
├── fe-admin/                   # React Admin Panel (Port 5173)
└── README.md
```

### Microservices

| Service | Port | Mô Tả | Database |
|---------|------|-------|----------|
| **DiscoveryService** | 8761 | Eureka Server - Service Registry | - |
| **GatewayService** | 9000 | API Gateway - Entry point cho tất cả requests | - |
| **UserService** | 8084 | Quản lý người dùng, authentication | PostgreSQL |
| **MenuService** | 8080 | Quản lý menu, món ăn | H2 |
| **BasketService** | 8081 | Quản lý giỏ hàng | Redis |
| **OrderingService** | 8082 | Quản lý đơn hàng | H2 |
| **PaymentService** | 8083 | Xử lý thanh toán | H2 |

### Infrastructure Services

| Service | Port | Credentials | Mô Tả |
|---------|------|-------------|-------|
| **RabbitMQ** | 5672, 15672 | guest/guest | Message Broker |
| **Redis** | 6379 | - | Cache & Session Store |
| **PostgreSQL** | 5432 | keycloak/keycloak | Database cho Keycloak & UserService |
| **Keycloak** | 8180 | admin/admin | SSO Authentication |

---

## 🚀 Hướng Dẫn Cài Đặt

### Bước 1: Clone Repository

```bash
git clone https://github.com/your-username/Beef-Restaurant.git
cd Beef-Restaurant
```

### Bước 2: Cài Đặt Dependencies

#### Backend (BuildingBlocks - Shared Library)

```bash
cd Backend/BuildingBlocks
mvn clean install
cd ../..
```

#### Frontend (Customer App)

```bash
cd Frontend
pnpm install
cd ..
```

#### Admin Panel

```bash
cd fe-admin
npm install
cd ..
```

---

## ⚙️ Cấu Hình Môi Trường

### Backend Environment Variables

Tạo file `.env` trong thư mục `Backend/` (hoặc sử dụng file có sẵn):

```bash
# Backend/.env
# Các biến môi trường cho microservices
# File này đã được cấu hình sẵn trong dự án
```

### Frontend Environment Variables

Tạo file `.env` trong thư mục `Frontend/`:

```bash
cd Frontend
cp .env.example .env
```

Chỉnh sửa `Frontend/.env`:

```env
# API Gateway Base URL
NEXT_PUBLIC_API_BASE=http://localhost:9000

# NextAuth Configuration
NEXTAUTH_URL=http://localhost:3000
NEXTAUTH_SECRET=your-secret-key-here-change-in-production

# Keycloak Configuration
KEYCLOAK_CLIENT_ID=your-client-id
KEYCLOAK_CLIENT_SECRET=your-client-secret
KEYCLOAK_ISSUER=http://localhost:8180/realms/master
```

### Admin Panel Environment Variables

Tạo file `.env` trong thư mục `fe-admin/`:

```bash
cd fe-admin
```

Tạo file `.env`:

```env
# API Gateway Base URL
VITE_API_BASE_URL=http://localhost:9000
```

---

## 🎮 Chạy Dự Án

### Option 1: Chạy Toàn Bộ Hệ Thống (Khuyến Nghị)

#### 1. Khởi Động Infrastructure Services (Docker)

```bash
cd Backend/Docker-Compose
docker compose up -d
```

Chờ tất cả services khởi động (khoảng 1-2 phút). Kiểm tra:

```bash
docker compose ps
```

#### 2. Cấu Hình Keycloak (Chỉ Lần Đầu)

1. Truy cập: http://localhost:8180
2. Đăng nhập với `admin/admin`
3. Tạo Realm hoặc sử dụng `master` realm
4. Tạo Client cho Frontend và Admin
5. Lấy Client ID và Secret, cập nhật vào `.env` files

#### 3. Build và Chạy Backend Services

**Option A: Chạy từng service riêng lẻ (Development)**

Mở terminal riêng cho mỗi service:

```bash
# Terminal 1 - Discovery Service
cd Backend/DiscoveryService
mvn spring-boot:run

# Terminal 2 - Gateway Service (chờ Discovery Service khởi động xong)
cd Backend/GatewayService
mvn spring-boot:run

# Terminal 3 - User Service
cd Backend/UserService
mvn spring-boot:run

# Terminal 4 - Menu Service
cd Backend/MenuService
mvn spring-boot:run

# Terminal 5 - Basket Service
cd Backend/BasketService
mvn spring-boot:run

# Terminal 6 - Ordering Service
cd Backend/OrderingService
mvn spring-boot:run

# Terminal 7 - Payment Service
cd Backend/PaymentService
mvn spring-boot:run
```

**Option B: Build Docker images và chạy (Production-like)**

Uncomment các services trong `Backend/Docker-Compose/docker-compose.yml` và:

```bash
# Build images cho tất cả services
cd Backend
mvn clean package -DskipTests

# Hoặc build từng service
cd UserService
mvn clean package -DskipTests
# Lặp lại cho các services khác

# Chạy tất cả với Docker Compose
cd ../Docker-Compose
docker compose up -d
```

#### 4. Chạy Frontend (Customer App)

```bash
cd Frontend
pnpm run dev
```

Frontend sẽ chạy tại: http://localhost:3000

#### 5. Chạy Admin Panel

```bash
cd fe-admin
npm run dev
```

Admin Panel sẽ chạy tại: http://localhost:5173

---

### Option 2: Chạy Chỉ Infrastructure (Để Development)

Nếu bạn chỉ muốn chạy RabbitMQ, Redis, PostgreSQL, Keycloak:

```bash
cd Backend/Docker-Compose
docker compose up -d rabbitmq redis postgres keycloak
```

Sau đó chạy các Backend services bằng IDE hoặc `mvn spring-boot:run`.

---

## 🌐 Truy Cập Ứng Dụng

### Applications

| Application | URL | Mô Tả |
|-------------|-----|-------|
| **Customer Frontend** | http://localhost:3000 | Trang web khách hàng |
| **Admin Panel** | http://localhost:5173 | Trang quản trị |
| **API Gateway** | http://localhost:9000 | REST API endpoint |

### Infrastructure UIs

| Service | URL | Credentials | Mô Tả |
|---------|-----|-------------|-------|
| **RabbitMQ Management** | http://localhost:15672 | guest/guest | Quản lý message queues |
| **Keycloak Admin** | http://localhost:8180 | admin/admin | Quản lý authentication |
| **Eureka Dashboard** | http://localhost:8761 | - | Service registry dashboard |

### API Endpoints (qua Gateway)

Tất cả requests đều đi qua Gateway tại `http://localhost:9000`:

- **User API**: `http://localhost:9000/api/user/**`
- **Menu API**: `http://localhost:9000/api/menu/**`
- **Basket API**: `http://localhost:9000/api/basket/**`
- **Order API**: `http://localhost:9000/api/order/**`
- **Payment API**: `http://localhost:9000/api/payment/**`

---

## 🔧 Xử Lý Sự Cố

### 1. Docker Services Không Khởi Động

```bash
# Kiểm tra logs
cd Backend/Docker-Compose
docker compose logs

# Restart services
docker compose restart

# Xóa và tạo lại
docker compose down -v
docker compose up -d
```

### 2. Port Đã Được Sử Dụng

Kiểm tra và kill process đang dùng port:

```powershell
# Windows PowerShell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### 3. Maven Build Lỗi

```bash
# Clean và rebuild
mvn clean install -DskipTests

# Nếu vẫn lỗi, xóa cache Maven
rm -rf ~/.m2/repository
mvn clean install
```

### 4. Frontend/Admin Không Kết Nối Được Backend

- Kiểm tra Gateway đã chạy: http://localhost:9000
- Kiểm tra file `.env` đã cấu hình đúng `NEXT_PUBLIC_API_BASE` hoặc `VITE_API_BASE_URL`
- Kiểm tra CORS settings trong Gateway
- Xem logs của Gateway Service

### 5. Keycloak Authentication Lỗi

- Đảm bảo Keycloak đã chạy: http://localhost:8180
- Kiểm tra Realm và Client đã được tạo
- Verify Client ID và Secret trong `.env` files
- Kiểm tra Redirect URIs trong Keycloak Client settings

### 6. Database Connection Lỗi

```bash
# Kiểm tra PostgreSQL
docker compose logs postgres

# Restart PostgreSQL
docker compose restart postgres

# Kiểm tra connection
docker compose exec postgres psql -U keycloak -d keycloak
```

### 7. RabbitMQ Connection Lỗi

```bash
# Kiểm tra RabbitMQ
docker compose logs rabbitmq

# Restart RabbitMQ
docker compose restart rabbitmq

# Truy cập management UI
# http://localhost:15672 (guest/guest)
```

### 8. Redis Connection Lỗi

```bash
# Kiểm tra Redis
docker compose logs redis

# Test connection
docker compose exec redis redis-cli ping
# Kết quả: PONG
```

---

## 📚 Tài Liệu Bổ Sung

### Thứ Tự Khởi Động Services (Quan Trọng)

1. **Infrastructure**: RabbitMQ, Redis, PostgreSQL, Keycloak
2. **Discovery Service** (Eureka)
3. **Gateway Service**
4. **Business Services**: User, Menu, Basket, Ordering, Payment (thứ tự tùy ý)
5. **Frontend & Admin**

### Technology Stack

**Backend:**
- Spring Boot 3.5.6
- Spring Cloud 2025.0.0
- Java 17
- Maven
- Eureka (Service Discovery)
- Spring Cloud Gateway
- Spring Security + OAuth2
- Keycloak
- PostgreSQL, H2
- Redis
- RabbitMQ
- Flyway (Database Migration)

**Frontend:**
- Next.js 14.2.5
- React 18
- TypeScript
- Tailwind CSS
- NextAuth.js
- Axios

**Admin:**
- React 19
- Vite 7
- React Router DOM
- Axios

---

## 👥 Đóng Góp

Nếu bạn muốn đóng góp cho dự án, vui lòng:

1. Fork repository
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

---

## 📝 License

Dự án này được phân phối dưới giấy phép [MIT License](LICENSE).

---

## 📞 Liên Hệ

Nếu có bất kỳ câu hỏi nào, vui lòng tạo issue trên GitHub repository.

---

**Happy Coding! 🚀**
