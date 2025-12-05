# UserService Architecture - Login/Register System

## 📋 Tổng Quan Kiến Trúc

Hệ thống được thiết kế với 2 thành phần chính:

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend/Client                           │
└─────────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
   ┌─────────┐         ┌──────────┐       ┌──────────┐
   │ Register│         │  Login   │       │ Logout   │
   └────┬────┘         └────┬─────┘       └────┬─────┘
        │                   │                   │
        └───────────────────┼───────────────────┘
                            │
                    ┌───────▼────────┐
                    │ UserService    │ (Port 8085)
                    │ Auth Endpoints │
                    └───┬────────┬───┘
                        │        │
        ┌───────────────┘        └──────────────┬──────────┐
        │                                       │          │
        ▼                                       ▼          ▼
   ┌─────────────┐                         ┌────────────────┐
   │ User DB     │                         │ Keycloak       │
   │ (H2/MySQL)  │                         │ (Port 8180)    │
   │             │                         │ OAuth2 Server  │
   │ - buyerId   │                         │                │
   │ - email     │                         │ - User Account │
   │ - name      │                         │ - Tokens       │
   │ - keycloakId│                         │ - Permissions  │
   └─────────────┘                         └────────────────┘
```

---

## 🔄 Luồng Hoạt Động

### 1. **REGISTER Flow**

```
Client → POST /api/v1/auth/register
    ├─ Request: { email, password, name, lastName }
    │
    ▼
UserService.register()
    ├─ 1️⃣ Kiểm tra email tồn tại?
    ├─ 2️⃣ Gọi KeycloakService.registerUser()
    │   └─ Tạo user trên Keycloak
    │       └─ Trả về keycloakUserId
    │
    ├─ 3️⃣ Lưu UserProfile vào database
    │   ├─ buyerId: UUID (khóa chính cho mục đích business)
    │   ├─ email: email (có thể dùng để gửi thông báo)
    │   ├─ keycloakId: ID từ Keycloak (link to identity provider)
    │   ├─ name, lastName, address info
    │   └─ Save to H2/MySQL
    │
    └─ Response: UserDTO với các thông tin cơ bản
```

### 2. **LOGIN Flow**

```
Client → POST /api/v1/auth/login
    ├─ Request: { email, password }
    │
    ▼
UserService.login()
    ├─ 1️⃣ Gọi KeycloakService.login(email, password)
    │   └─ Keycloak verify credentials
    │       └─ Return: accessToken, refreshToken, expiresIn
    │
    ├─ 2️⃣ Lấy user từ database theo email
    │   └─ Đảm bảo user tồn tại trong hệ thống
    │
    └─ Response: LoginResponse
        ├─ accessToken (JWT từ Keycloak)
        ├─ refreshToken
        ├─ expiresIn
        └─ user: { buyerId, email, name, ... }
```

### 3. **LOGOUT Flow**

```
Client → POST /api/v1/auth/logout?refreshToken=xxx
    │
    ▼
UserService.logout()
    └─ KeycloakService.logout(refreshToken)
       └─ Keycloak invalidate refresh token
           └─ User không thể dùng token này để login lại
```

### 4. **REFRESH TOKEN Flow**

```
Client → POST /api/v1/auth/refresh?refreshToken=xxx
    │
    ▼
KeycloakService.refreshToken()
    └─ Keycloak cấp accessToken mới
       └─ Refresh token vẫn giữ nguyên hoặc cấp mới
```

---

## 📁 Cấu Trúc Database

### Bảng `eshop_users`:

```sql
CREATE TABLE eshop_users (
    buyer_id VARCHAR(255) PRIMARY KEY,           -- Business ID (UUID)
    email VARCHAR(255) NOT NULL UNIQUE,          -- Email (dùng để gửi thông báo)
    name VARCHAR(255),                            -- First name
    last_name VARCHAR(255),                       -- Last name
    street VARCHAR(255),                          -- Address info
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255),
    keycloak_id VARCHAR(255) NOT NULL,           -- Link to Keycloak
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Quan trọng:**

- **keycloak_id**: Reference đến user ID trên Keycloak (không thay đổi)
- **email**: Email người dùng (dùng để login và gửi thông báo)
- **buyerId**: Business ID (UUID) - dùng cho mục đích business

---

## 🔐 Token Management

### Access Token

- **Cấp bởi:** Keycloak
- **Format:** JWT
- **Thời gian sống:** ~5 phút (config Keycloak)
- **Dùng để:** Xác thực API requests
- **Lưu ở:** Browser localStorage/sessionStorage
- **Header:** `Authorization: Bearer <accessToken>`

### Refresh Token

- **Cấp bởi:** Keycloak
- **Thời gian sống:** ~30 ngày (config Keycloak)
- **Dùng để:** Lấy access token mới khi hết hạn
- **Lưu ở:** Browser (httpOnly cookie hoặc localStorage)
- **Không dùng cho:** Xác thực API requests

---

## 🔄 Data Sync Strategy

### Tại sao cần sync?

1. **Keycloak** là source of truth cho authentication (username/email, password hash)
2. **UserService DB** lưu additional profile data (address, phone, etc.)

### Khi nào sync xảy ra?

| Sự kiện         | UserService DB     | Keycloak            |
| --------------- | ------------------ | ------------------- |
| Register        | ✅ Tạo UserProfile | ✅ Tạo User Account |
| Login           | ❌ Chỉ verify      | ✅ Verify password  |
| Update Profile  | ✅ Cập nhật info   | ❌ Không cập nhật   |
| Delete User     | ❌ Keep record     | ✅ Xóa account      |
| Change Password | ❌ Không handle    | ✅ Update           |

### Best Practices:

1. **UserService DB** là "read-mostly" cho profile info
2. **Keycloak** quản lý security credentials
3. **Email là link giữa 2 hệ thống** (unique constraint ở DB)
4. **Tất cả auth operations** đi qua Keycloak

---

## 🛠️ Configuration Cần Thiết

### 1. Keycloak Setup

**Tạo Client:**

- Realm: `master`
- Client ID: `user-service-client`
- Access Type: `confidential`
- Valid Redirect URIs: `http://localhost:3000/*`
- Web Origins: `http://localhost:3000`

**Lấy Client Secret:**

- Tab "Credentials" → Copy Secret

### 2. UserService Config (application.yaml)

```yaml
keycloak:
  server-url: http://localhost:8180
  realm: master
  client-id: user-service-client
  client-secret: <copy-from-keycloak>
```

### 3. Environment Variables

```bash
export KEYCLOAK_CLIENT_SECRET="<client-secret-from-keycloak>"
```

---

## 📡 API Endpoints

### Authentication Endpoints (No Auth Required)

```
POST /api/v1/auth/register
  Body: { email, password, name, lastName }
  Response: UserDTO

POST /api/v1/auth/login
  Body: { email, password }
  Response: LoginResponse (accessToken, refreshToken, user)

POST /api/v1/auth/logout
  Params: ?refreshToken=xxx
  Response: 200 OK

POST /api/v1/auth/refresh
  Params: ?refreshToken=xxx
  Response: LoginResponse (new tokens)
```

### User Endpoints (Requires Auth)

```
GET /api/v1/auth/me
  Headers: Authorization: Bearer <accessToken>
  Response: UserDTO

PUT /api/v1/auth/me
  Headers: Authorization: Bearer <accessToken>
  Body: { name, lastName, street, city, state, country }
  Response: UserDTO

GET /api/v1/users
  Headers: Authorization: Bearer <accessToken> + Role: ADMIN
  Response: List<UserProfile>

GET /api/v1/users/{userId}
  Headers: Authorization: Bearer <accessToken> + Role: ADMIN
  Response: UserProfile

DELETE /api/v1/users/{userId}
  Headers: Authorization: Bearer <accessToken> + Role: ADMIN
  Response: 204 No Content
```

---

## 🧪 Testing Flow

### 1. Register

```bash
curl -X POST http://localhost:8085/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "Password123",
    "name": "John",
    "lastName": "Doe"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8085/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "Password123"
  }'
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "xxx",
  "expiresIn": 300,
  "tokenType": "Bearer",
  "user": {
    "buyerId": "550e8400-e29b-41d4-a716-446655440000",
    "email": "user@example.com",
    "name": "John",
    "lastName": "Doe"
  }
}
```

### 3. Get My Profile

```bash
curl -X GET http://localhost:8085/api/v1/auth/me \
  -H "Authorization: Bearer <accessToken>"
```

### 4. Logout

```bash
curl -X POST http://localhost:8085/api/v1/auth/logout \
  -H "Authorization: Bearer <accessToken>" \
  -d "refreshToken=<refreshToken>"
```

---

## 📝 Lưu Ý Quan Trọng

1. **Email là unique** → Không thể có 2 user cùng email
2. **keycloakId không thay đổi** → Để link safely giữa hệ thống
3. **Password không bao giờ lưu trong UserService DB** → Keycloak quản lý
4. **Access Token chứa Keycloak ID** → Extract từ `jwt.getSubject()`
5. **Refresh Token cần secure storage** → Preferably HttpOnly Cookie
6. **Email dùng cho:** Login, notifications, account recovery

---

## ⚠️ Security Considerations

1. **Keycloak Server:** Phải HTTPS trong production
2. **Client Secret:** Không commit vào git, dùng environment variables
3. **Tokens:** Không log/print tokens, chỉ log timestamps
4. **CORS:** Chỉ allow frontend domain
5. **Rate Limiting:** Add rate limit cho auth endpoints
6. **Audit:** Log tất cả auth operations

---

## 📊 Diagram - Data Flow Chi Tiết

```
REGISTRATION:
┌─────────────────────────────────────────────────────────────┐
│ 1. Client POST /register {email, password, name}            │
│ 2. AuthController → UserService.register()                 │
│ 3. UserService → KeycloakService.registerUser()            │
│ 4. Keycloak: Tạo user account, verify email               │
│ 5. Keycloak ← Return keycloakUserId                        │
│ 6. UserService: Tạo UserProfile entity                    │
│ 7. UserProfile save to DB:                                 │
│    - buyerId: UUID.randomUUID()                            │
│    - email: from request                                    │
│    - keycloak_id: from Keycloak                           │
│    - name, lastName, etc.                                  │
│ 8. Response: UserDTO (without password)                   │
│ 9. Client: Hiển thị success → Redirect to login           │
└─────────────────────────────────────────────────────────────┘

LOGIN:
┌─────────────────────────────────────────────────────────────┐
│ 1. Client POST /login {email, password}                    │
│ 2. AuthController → UserService.login()                    │
│ 3. UserService → KeycloakService.login()                  │
│ 4. Keycloak: Verify email + password                       │
│ 5. Keycloak ← Return tokens if valid                       │
│ 6. UserService: findByEmail() from DB                      │
│ 7. Response: LoginResponse {accessToken, refreshToken, user}
│ 8. Client: Save tokens, Set Authorization header          │
│ 9. Client: Can now access protected endpoints             │
└─────────────────────────────────────────────────────────────┘

AUTHENTICATED REQUEST:
┌─────────────────────────────────────────────────────────────┐
│ 1. Client GET /api/v1/auth/me                              │
│    Header: Authorization: Bearer <accessToken>            │
│ 2. SecurityConfig: Filter JWT token                        │
│ 3. Extract keycloakId from token subject                   │
│ 4. @AuthenticationPrincipal Jwt jwt                        │
│ 5. UserService.getMyProfile(jwt.getSubject())             │
│ 6. Repository.findByKeycloakId(keycloakId)                │
│ 7. Response: UserDTO with profile info                    │
└─────────────────────────────────────────────────────────────┘
```
