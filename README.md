# Setup Hướng Dẫn Cho Team Members

## Yêu Cầu Hệ Thống

- **Java**: JDK 17+
- **Maven**: 3.6+
- **Docker Desktop**: Đang chạy
- **RAM**: 8GB+ (khuyến nghị 16GB)
- **Git**: Để clone repository

## Bước 1: Clone Repository

```bash
git clone <repository-url>
cd Beef-Restaurant
```

## Bước 2: Lấy File Keycloak Realm Export

**QUAN TRỌNG**: File Keycloak realm export chứa cấu hình authentication và KHÔNG có trong Git (đã bị gitignore vì chứa thông tin nhạy cảm).

### Cách Lấy File:

**Option A: Từ Admin/Lead** (Khuyến nghị)
1. Liên hệ admin/lead để lấy file `realm-export.json`
2. Copy file vào: `Backend/Docker-Compose/keycloak/`

**Option B: Tự Setup Keycloak** (Nếu không có file)
- Xem hướng dẫn trong `Backend/Docker-Compose/KEYCLOAK_SETUP.md`
- Section "Manual Setup"

## Bước 3: Build Docker Images

### Windows (PowerShell):
```powershell
cd Backend\Docker-Compose\scripts
.\build-all-services.ps1
```

### Mac/Linux (Bash):
```bash
cd Backend/Docker-Compose/scripts
chmod +x build-all-services.sh
./build-all-services.sh
```

⏱️ **Thời gian**: 5-10 phút cho lần đầu

**Kết quả**: Sẽ có 7 Docker images được build:
- minh28012004/discovery-service:latest
- minh28012004/gateway-service:latest
- minh28012004/user-service:latest
- minh28012004/menu-service:latest
- minh28012004/basket-service:latest
- minh28012004/ordering-service:latest
- minh28012004/payment-service:latest

## Bước 4: Kiểm Tra Images

```bash
docker images | grep minh28012004
```

Phải thấy đầy đủ 7 images ở trên.

## Bước 5: Start Dự Án

```bash
cd Backend/Docker-Compose
docker-compose up -d
```

**Chờ 2-3 phút** để tất cả services khởi động.

## Bước 6: Kiểm Tra Services

```bash
docker-compose ps
```

Tất cả services phải ở trạng thái `Up` hoặc `healthy`.

## Bước 7: Truy Cập Ứng Dụng

### Backend Services
- **API Gateway**: http://localhost:9000
- **Eureka Dashboard**: http://localhost:8761
- **Keycloak Admin**: http://localhost:8180 (admin/admin)
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

### Frontend
```bash
cd ../../fe-admin
npm install
npm run dev
```

- **Admin Dashboard**: http://localhost:5173

## Xử Lý Lỗi Thường Gặp

### Lỗi 1: "Cannot find BuildingBlocks"
```bash
cd Backend/BuildingBlocks
mvn clean install
```

### Lỗi 2: "Port already in use"
Dừng service đang dùng port hoặc đổi port trong `docker-compose.yml`.

### Lỗi 3: Keycloak không import realm
1. Kiểm tra file `Backend/Docker-Compose/keycloak/realm-export.json` có tồn tại không
2. Nếu không có, liên hệ admin để lấy file
3. Restart Keycloak:
   ```bash
   docker-compose restart keycloak
   ```

### Lỗi 4: Services không kết nối được
Đợi thêm 1-2 phút. Services khởi động theo thứ tự:
1. Infrastructure (RabbitMQ, Redis, PostgreSQL)
2. Keycloak
3. DiscoveryService
4. GatewayService
5. Các microservices khác

### Lỗi 5: Out of memory
Tăng Docker memory:
- Docker Desktop → Settings → Resources → Memory → 4GB+

## Workflow Development

### Khi Sửa Code Một Service

1. **Sửa code** (vd: UserService)
2. **Build lại**:
   ```bash
   cd Backend/Docker-Compose/scripts
   ./build-single-service.sh UserService  # Mac/Linux
   # hoặc
   .\build-single-service.ps1 UserService  # Windows (nếu có)
   ```
3. **Restart service**:
   ```bash
   cd ../
   docker-compose restart user-service
   ```
4. **Test**

### Xem Logs

```bash
# Tất cả services
docker-compose logs -f

# Một service cụ thể
docker-compose logs -f user-service

# 100 dòng cuối
docker-compose logs --tail=100 user-service
```

### Dừng Dự Án

```bash
# Dừng (giữ data)
docker-compose stop

# Dừng và xóa containers (giữ data)
docker-compose down

# Xóa tất cả (mất data)
docker-compose down -v
```

## Cấu Trúc Dự Án

```
Beef-Restaurant/
├── Backend/
│   ├── Docker-Compose/
│   │   ├── scripts/
│   │   │   ├── build-all-services.ps1
│   │   │   ├── build-all-services.sh
│   │   │   └── export-keycloak-realm.ps1
│   │   ├── keycloak/
│   │   │   └── realm-export.json (KHÔNG có trong Git - phải lấy riêng)
│   │   ├── docker-compose.yml
│   │   ├── BUILD_GUIDE.md
│   │   └── KEYCLOAK_SETUP.md
│   ├── DiscoveryService/
│   ├── GatewayService/
│   ├── UserService/
│   ├── MenuService/
│   ├── BasketService/
│   ├── OrderingService/
│   ├── PaymentService/
│   └── BuildingBlocks/
└── fe-admin/
    └── (React admin dashboard)
```

## Ports Reference

| Service | Port | URL |
|---------|------|-----|
| Gateway | 9000 | http://localhost:9000 |
| Keycloak | 8180 | http://localhost:8180 |
| Eureka | 8761 | http://localhost:8761 |
| RabbitMQ UI | 15672 | http://localhost:15672 |
| UserService | 8084 | http://localhost:8084 |
| MenuService | 8080 | http://localhost:8080 |
| BasketService | 8081 | http://localhost:8081 |
| OrderingService | 8082 | http://localhost:8082 |
| PaymentService | 8083 | http://localhost:8083 |
| Admin Dashboard | 5173 | http://localhost:5173 |

## Tài Liệu Tham Khảo

- **BUILD_GUIDE.md**: Hướng dẫn build chi tiết, troubleshooting
- **KEYCLOAK_SETUP.md**: Setup Keycloak, export/import realm
- **Backend/README.md**: Tổng quan kiến trúc hệ thống

## Liên Hệ

Nếu gặp vấn đề, liên hệ:
- **Admin/Lead**: [Tên người quản lý]
- **Channel**: [Slack/Teams channel]

## Checklist Setup

- [ ] Clone repository
- [ ] Lấy file `realm-export.json` từ admin
- [ ] Copy file vào `Backend/Docker-Compose/keycloak/`
- [ ] Build tất cả services (`build-all-services.ps1`)
- [ ] Kiểm tra images (`docker images | grep minh28012004`)
- [ ] Start docker-compose (`docker-compose up -d`)
- [ ] Kiểm tra services (`docker-compose ps`)
- [ ] Test API Gateway (http://localhost:9000)
- [ ] Test Keycloak (http://localhost:8180)
- [ ] Install frontend dependencies (`npm install`)
- [ ] Start frontend (`npm run dev`)
- [ ] Test admin dashboard (http://localhost:5173)

✅ **Setup hoàn tất!**
