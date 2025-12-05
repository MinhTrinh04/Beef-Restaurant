# Keycloak Setup Guide

## 🚀 Bước 1: Start Keycloak Server

### Option 1: Docker (Recommended)

```bash
docker run --name keycloak \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -p 8180:8080 \
  quay.io/keycloak/keycloak:latest \
  start-dev
```

### Option 2: Docker Compose

```yaml
version: "3.8"
services:
  keycloak:
    image: quay.io/keycloak/keycloak:latest
    ports:
      - "8180:8080"
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
    command:
      - start-dev
```

```bash
docker-compose up -d
```

### Option 3: Manual Download

1. Download from https://www.keycloak.org/downloads
2. Extract to a directory
3. Run: `./bin/kc.sh start-dev`

---

## 📋 Bước 2: Truy cập Admin Console

1. Mở browser: http://localhost:8180
2. Click **"Administration Console"**
3. Login với:
   - Username: `admin`
   - Password: `admin`

---

## 🔧 Bước 3: Tạo Client cho UserService

### 3.1 Chọn Realm

- Chọn realm **"master"** (hoặc tạo realm mới nếu muốn)

### 3.2 Tạo Client

1. Sidebar → **Clients** → **Create client**
2. Điền thông tin:
   - **Client ID:** `user-service-client`
   - **Name:** User Service Client
   - **Enabled:** ON
3. Click **Next**

### 3.3 Capability Config

1. **Client authentication:** ON
2. **Authorization:** OFF
3. Click **Next**

### 3.4 Login Settings

1. **Valid redirect URIs:**
   ```
   http://localhost:3000/*
   http://localhost:8085/*
   ```
2. **Web origins:**
   ```
   http://localhost:3000
   http://localhost:8085
   ```
3. **Access token lifespan:** 5 minutes
4. Click **Save**

### 3.5 Lấy Client Secret

1. Tab **"Credentials"**
2. Copy **Client secret**

```
Example:
abc123def456ghi789jkl012mno345pqr678stu901vwx
```

---

## 👤 Bước 4: Tạo Test User (Optional)

1. Sidebar → **Users** → **Create new user**
2. Điền:
   - **Username:** testuser@example.com
   - **Email:** testuser@example.com
   - **Email verified:** ON
   - **Enabled:** ON
3. Click **Create**

### Set Password

1. Tab **"Credentials"**
2. Click **"Set password"**
3. Điền password: `TestPassword123`
4. **Temporary:** OFF
5. Click **Set password**

---

## 🛠️ Bước 5: Configure UserService

### Update application.yaml

```yaml
keycloak:
  server-url: http://localhost:8180
  realm: master
  client-id: user-service-client
  client-secret: abc123def456ghi789jkl012mno345pqr678stu901vwx
```

### Via Environment Variable

```bash
export KEYCLOAK_CLIENT_SECRET="abc123def456ghi789jkl012mno345pqr678stu901vwx"
```

---

## ✅ Bước 6: Test Integration

### 1. Register New User

```bash
curl -X POST http://localhost:8085/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "Password123!",
    "name": "John",
    "lastName": "Doe"
  }'
```

**Expected Response (201):**

```json
{
  "buyerId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "john@example.com",
  "name": "John",
  "lastName": "Doe",
  "street": null,
  "city": null,
  "state": null,
  "country": null
}
```

### 2. Login

```bash
curl -X POST http://localhost:8085/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "Password123!"
  }'
```

**Expected Response (200):**

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ii1GQTQ2ZjdlY0s5LWV3a3pRandOSl9jdnhxbW5wMHJKbU9ueFJQZy0zZjAifQ...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlMjQ2...",
  "expiresIn": 300,
  "tokenType": "Bearer",
  "user": {
    "buyerId": "550e8400-e29b-41d4-a716-446655440000",
    "email": "john@example.com",
    "name": "John",
    "lastName": "Doe",
    "street": null,
    "city": null,
    "state": null,
    "country": null
  }
}
```

### 3. Get My Profile (With Auth)

```bash
curl -X GET http://localhost:8085/api/v1/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 4. Logout

```bash
curl -X POST "http://localhost:8085/api/v1/auth/logout?refreshToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 5. Refresh Token

```bash
curl -X POST "http://localhost:8085/api/v1/auth/refresh?refreshToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 🔑 Token Structure

### Access Token (JWT)

Decode token trên jwt.io để xem:

```json
{
  "iss": "http://localhost:8180/realms/master",
  "sub": "abc123def456", // Keycloak User ID
  "aud": "account",
  "email": "john@example.com",
  "exp": 1702345678,
  "iat": 1702345378,
  "name": "John",
  "given_name": "John",
  "family_name": "Doe",
  "email_verified": true,
  "realm_access": {
    "roles": ["offline_access", "uma_authorization", "user"]
  }
}
```

---

## 🚨 Troubleshooting

### Error: "Failed to register user"

**Cause:** Email already exists in Keycloak
**Solution:** Use unique email

### Error: "Login failed"

**Cause 1:** Wrong email/password
**Solution:** Check credentials, ensure user is enabled

**Cause 2:** Client secret wrong
**Solution:** Copy correct secret from Keycloak credentials

### Error: "Invalid token"

**Cause:** Token expired or issuer mismatch
**Solution:**

1. Refresh token
2. Check keycloak issuer-uri in application.yaml

### Keycloak Admin Password Reset

```bash
docker exec keycloak /opt/keycloak/bin/kcadm.sh create realms \
  -s realm=master \
  -s enabled=true \
  --no-config --server http://localhost:8080 \
  --realm master \
  --user admin \
  --password newpassword
```

---

## 📚 Useful Keycloak Admin API Endpoints

### Get all users

```bash
curl -X GET http://localhost:8180/admin/realms/master/users \
  -H "Authorization: Bearer <admin-token>"
```

### Get user by ID

```bash
curl -X GET http://localhost:8180/admin/realms/master/users/<user-id> \
  -H "Authorization: Bearer <admin-token>"
```

### Delete user

```bash
curl -X DELETE http://localhost:8180/admin/realms/master/users/<user-id> \
  -H "Authorization: Bearer <admin-token>"
```

---

## 🔒 Production Checklist

- [ ] Use HTTPS for Keycloak server
- [ ] Change admin password
- [ ] Enable SMTP for email verification
- [ ] Configure password policies
- [ ] Enable audit logging
- [ ] Set up SSL certificate
- [ ] Configure backup strategy
- [ ] Restrict CORS origins
- [ ] Enable rate limiting
- [ ] Set up monitoring/alerts
