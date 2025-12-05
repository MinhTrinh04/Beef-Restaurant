# 🎯 Summary of Changes

## 📝 Tổng Quan Các Thay Đổi

### Vấn đề Ban Đầu:

- ❌ Không có endpoints register/login
- ❌ UserService database không sync với Keycloak
- ❌ Không thể đăng nhập bằng email

### Giải Pháp Triển Khai:

- ✅ Thêm endpoints register, login, logout, refresh
- ✅ Sync UserService DB với Keycloak thông qua keycloakId
- ✅ Login/Register bằng email
- ✅ Keycloak làm OAuth2 authorization server
- ✅ UserService quản lý profile & identity link

---

## 📁 Files Được Tạo/Sửa

### 1. **Dependencies (pom.xml)**

✅ Thêm Keycloak library
✅ Thêm Gson library (JSON parsing)

### 2. **Entity Model**

**UserProfile.java** - Updated

- Thêm field `email` (unique, not null)
- Thêm field `keycloakId` (link to Keycloak)
- Thêm fields `created_at`, `updated_at`

### 3. **Database Schema**

**V1\_\_schema.sql** - Updated

- Thêm cột `email` (unique)
- Thêm cột `keycloak_id`
- Thêm cột `created_at`, `updated_at`

**V2\_\_add_email_and_keycloak_id.sql** - Created

- Migration file cho update existing databases

### 4. **Repository**

**UserRepository.java** - Updated

- Thêm method `findByEmail(String email)`
- Thêm method `findByKeycloakId(String keycloakId)`
- Thêm method `existsByEmail(String email)`

### 5. **Service Layer**

**KeycloakService.java** - Created ⭐

- `registerUser()` - Tạo user trên Keycloak
- `login()` - Verify credentials, cấp tokens
- `logout()` - Invalidate refresh token
- `refreshToken()` - Cấp access token mới
- `getUserInfo()` - Lấy thông tin user từ Keycloak
- `getAdminToken()` - Internal - lấy admin token

**UserService.java** - Created ⭐

- `register()` - Orchestrate registration
- `login()` - Orchestrate login
- `logout()` - Logout
- `refreshToken()` - Refresh access token
- `getMyProfile()` - Lấy profile người dùng
- `updateProfile()` - Cập nhật profile

### 6. **Controllers**

**AuthController.java** - Created ⭐

- `POST /api/v1/auth/register` - Register
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/logout` - Logout
- `POST /api/v1/auth/refresh` - Refresh token
- `GET /api/v1/auth/me` - Get my profile (auth)
- `PUT /api/v1/auth/me` - Update profile (auth)

**UserController.java** - Updated

- Thay `buyerId` → `keycloakId` cho lookups
- Giữ backward compatibility cho admin endpoints
- Thêm `@PreAuthorize` cho admin endpoints

### 7. **Configuration**

**SecurityConfig.java** - Updated

- Thêm permitAll() cho auth endpoints
- Giữ JWT authentication cho protected endpoints

**application.yaml** - Updated

- Thêm Keycloak server config

### 8. **DTOs - Created**

- **LoginRequest.java** - { email, password }
- **RegisterRequest.java** - { email, password, name, lastName }
- **LoginResponse.java** - { accessToken, refreshToken, expiresIn, user }
- **UserDTO.java** - Profile info

---

## 🔄 Data Flow - Trước vs Sau

### Trước (Incomplete):

```
Frontend
   ↓
UserService
   ↓
JWT from JWT.io (external)
   ↓
❌ Không có register/login
❌ Database bị isolate
```

### Sau (Complete):

```
Frontend
   ├─ POST /register → AuthController
   ├─ POST /login → AuthController
   └─ GET /auth/me (with token) → AuthController
        ↓
   AuthController
        ↓
   UserService (orchestration)
        ├─ KeycloakService (identity & auth)
        │  ├─ Tạo user account
        │  └─ Verify credentials
        └─ UserRepository (profile & link)
           ├─ Lưu user info
           └─ Link keycloakId
```

---

## 📊 Database Schema - Trước vs Sau

### Trước:

```sql
CREATE TABLE eshop_users (
    buyer_id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    last_name VARCHAR(255),
    street VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255)
);
```

### Sau:

```sql
CREATE TABLE eshop_users (
    buyer_id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,  -- ✅ NEW
    name VARCHAR(255),
    last_name VARCHAR(255),
    street VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255),
    keycloak_id VARCHAR(255) NOT NULL,   -- ✅ NEW (link to Keycloak)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- ✅ NEW
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- ✅ NEW
);
```

---

## 🔐 Security Improvements

1. **Password Management:**

   - ❌ Trước: Không có (hoặc unsafe)
   - ✅ Sau: Keycloak handles (bcrypt, salt, policies)

2. **Email Verification:**

   - ❌ Trước: Không có
   - ✅ Sau: Keycloak support

3. **Token Management:**

   - ❌ Trước: Manual JWT handling
   - ✅ Sau: OAuth2 flow with refresh tokens

4. **User Isolation:**
   - ✅ Vẫn giữ: Each user can only see/modify own data

---

## 🚀 Cách Sử Dụng

### 1. Setup Keycloak

```bash
docker run --name keycloak \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -p 8180:8080 \
  quay.io/keycloak/keycloak:latest \
  start-dev
```

### 2. Create Client trong Keycloak

- Realm: `master`
- Client ID: `user-service-client`
- Copy Client Secret

### 3. Update application.yaml

```yaml
keycloak:
  server-url: http://localhost:8180
  realm: master
  client-id: user-service-client
  client-secret: <your-secret>
```

### 4. Run UserService

```bash
cd Backend/UserService
mvn spring-boot:run
```

### 5. Register User

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

### 6. Login

```bash
curl -X POST http://localhost:8085/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "Password123"
  }'
```

---

## 📌 Key Points to Remember

1. **Email là primary identifier cho login** ✉️
2. **buyerId là business identifier** (UUID, dùng cho orders, etc)
3. **keycloakId là identity link** (không thay đổi, link to Keycloak)
4. **UserService DB + Keycloak sync bằng email + keycloakId**
5. **All passwords handled by Keycloak** (không bao giờ lưu ở UserService)
6. **Tokens từ Keycloak** (accessToken valid 5 min, refreshToken valid 30 days)

---

## 📚 Documentation Files

1. **ARCHITECTURE.md** - Detailed architecture & flows
2. **KEYCLOAK_SETUP.md** - Step-by-step Keycloak setup
3. **CHANGES.md** - This file

---

## ✅ Testing Checklist

- [ ] Run `mvn clean install`
- [ ] Start Keycloak
- [ ] Setup client in Keycloak
- [ ] Run UserService on port 8085
- [ ] Test `/api/v1/auth/register` ✅
- [ ] Test `/api/v1/auth/login` ✅
- [ ] Test `/api/v1/auth/me` with token ✅
- [ ] Test `/api/v1/auth/logout` ✅
- [ ] Test `/api/v1/auth/refresh` ✅
- [ ] Check database - user record created ✅
- [ ] Verify keycloakId saved in DB ✅
- [ ] Verify email unique constraint ✅

---

## 🔗 Frontend Integration

### Login Example (React/Vue):

```typescript
// 1. Register
const registerResponse = await fetch("/api/v1/auth/register", {
  method: "POST",
  body: JSON.stringify({ email, password, name, lastName }),
});

// 2. Login
const loginResponse = await fetch("/api/v1/auth/login", {
  method: "POST",
  body: JSON.stringify({ email, password }),
});

const { accessToken, refreshToken, user } = await loginResponse.json();

// 3. Store tokens
localStorage.setItem("accessToken", accessToken);
localStorage.setItem("refreshToken", refreshToken);

// 4. Use in requests
fetch("/api/v1/auth/me", {
  headers: {
    Authorization: `Bearer ${accessToken}`,
  },
});

// 5. Logout
await fetch(`/api/v1/auth/logout?refreshToken=${refreshToken}`, {
  method: "POST",
});
localStorage.removeItem("accessToken");
localStorage.removeItem("refreshToken");
```

---

## 🎓 Next Steps (Optional Enhancements)

1. **Email Verification** - Enable in Keycloak
2. **Social Login** - Add Google/Facebook providers
3. **Two-Factor Auth** - Enable TOTP in Keycloak
4. **Role-Based Access** - Assign roles to users
5. **API Rate Limiting** - Protect auth endpoints
6. **Audit Logging** - Log all auth events
7. **Session Management** - Track active sessions
8. **Account Recovery** - Forgot password flow
