# User Service API - CURL Commands for Testing

## Environment Variables (Set these first)

```bash
BASE_URL="http://localhost:8085"
ACCESS_TOKEN=""          # Will be set after login
REFRESH_TOKEN=""         # Will be set after login
ADMIN_TOKEN=""           # Need to get from admin login
USER_ID=""               # Will be set after register
EMAIL="john.doe@example.com"
PASSWORD="Password123!"
```

---

## 🔐 AUTHENTICATION ENDPOINTS

### 1. Register New User

```bash
curl -X POST "$BASE_URL/api/v1/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "Password123!",
    "name": "John",
    "lastName": "Doe"
  }'
```

**Expected Response (201):**

```json
{
  "buyerId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "john.doe@example.com",
  "name": "John",
  "lastName": "Doe",
  "street": null,
  "city": null,
  "state": null,
  "country": null
}
```

---

### 2. Register User - Duplicate Email (Should Fail)

```bash
curl -X POST "$BASE_URL/api/v1/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "DifferentPassword123!",
    "name": "Jane",
    "lastName": "Smith"
  }'
```

**Expected Response (409):** Email already registered

---

### 3. Register User - Invalid Email (Should Fail)

```bash
curl -X POST "$BASE_URL/api/v1/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "invalid-email",
    "password": "Password123!",
    "name": "Test",
    "lastName": "User"
  }'
```

**Expected Response (400):** Invalid email format

---

### 4. Register User - Password Too Short (Should Fail)

```bash
curl -X POST "$BASE_URL/api/v1/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Pass1",
    "name": "Test",
    "lastName": "User"
  }'
```

**Expected Response (400):** Password must be at least 8 characters

---

### 5. Login

```bash
curl -X POST "$BASE_URL/api/v1/users/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
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
    "email": "john.doe@example.com",
    "name": "John",
    "lastName": "Doe",
    "street": null,
    "city": null,
    "state": null,
    "country": null
  }
}
```

---

### 6. Login - Wrong Password (Should Fail)

```bash
curl -X POST "$BASE_URL/api/v1/users/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "WrongPassword123!"
  }'
```

**Expected Response (401):** Unauthorized

---

### 7. Login - Non-existent User (Should Fail)

```bash
curl -X POST "$BASE_URL/api/v1/users/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nonexistent@example.com",
    "password": "Password123!"
  }'
```

**Expected Response (401):** Unauthorized

---

### 8. Refresh Access Token

```bash
# First, save tokens from login response
ACCESS_TOKEN="<copy-from-login-response>"
REFRESH_TOKEN="<copy-from-login-response>"

# Then refresh
curl -X POST "$BASE_URL/api/v1/users/refresh?refreshToken=$REFRESH_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (200):**

```json
{
  "accessToken": "new-token-here",
  "refreshToken": "new-refresh-token-or-same",
  "expiresIn": 300,
  "tokenType": "Bearer"
}
```

---

### 9. Logout

```bash
curl -X POST "$BASE_URL/api/v1/users/logout?refreshToken=$REFRESH_TOKEN" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (200):** OK

---

## 👤 USER PROFILE ENDPOINTS

### 10. Get My Profile (Requires Auth)

```bash
curl -X GET "$BASE_URL/api/v1/users/me" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (200):**

```json
{
  "buyerId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "john.doe@example.com",
  "name": "John",
  "lastName": "Doe",
  "street": null,
  "city": null,
  "state": null,
  "country": null
}
```

---

### 11. Get My Profile - No Token (Should Fail)

```bash
curl -X GET "$BASE_URL/api/v1/users/me" \
  -H "Content-Type: application/json"
```

**Expected Response (401):** Unauthorized

---

### 12. Get My Profile - Invalid Token (Should Fail)

```bash
curl -X GET "$BASE_URL/api/v1/users/me" \
  -H "Authorization: Bearer invalid-token-12345" \
  -H "Content-Type: application/json"
```

**Expected Response (401):** Unauthorized

---

### 13. Update My Profile (Full Update)

```bash
curl -X PUT "$BASE_URL/api/v1/users/me" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "lastName": "Doe Updated",
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "country": "USA"
  }'
```

**Expected Response (200):**

```json
{
  "buyerId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "john.doe@example.com",
  "name": "John Updated",
  "lastName": "Doe Updated",
  "street": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "country": "USA"
}
```

---

### 14. Update My Profile (Partial Update)

```bash
curl -X PUT "$BASE_URL/api/v1/users/me" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "city": "Ho Chi Minh City",
    "country": "Vietnam"
  }'
```

**Expected Response (200):** Updated profile

---

### 15. Update My Profile - No Token (Should Fail)

```bash
curl -X PUT "$BASE_URL/api/v1/users/me" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane"
  }'
```

**Expected Response (401):** Unauthorized

---

## 👮 ADMIN ENDPOINTS

### 16. Get All Users (Admin Only)

```bash
curl -X GET "$BASE_URL/api/v1/users" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (200):**

```json
[
  {
    "buyerId": "550e8400-e29b-41d4-a716-446655440000",
    "email": "john.doe@example.com",
    "name": "John",
    "lastName": "Doe",
    ...
  },
  {
    "buyerId": "660e8400-e29b-41d4-a716-446655440001",
    "email": "jane.smith@example.com",
    "name": "Jane",
    "lastName": "Smith",
    ...
  }
]
```

---

### 17. Get All Users - Non-Admin (Should Fail)

```bash
curl -X GET "$BASE_URL/api/v1/users" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (403):** Forbidden - Access Denied

---

### 18. Get User By ID (Admin Only)

```bash
# First get a valid user ID from register or get all users
USER_ID="550e8400-e29b-41d4-a716-446655440000"

curl -X GET "$BASE_URL/api/v1/users/$USER_ID" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (200):**

```json
{
  "buyerId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "john.doe@example.com",
  "name": "John",
  "lastName": "Doe",
  "street": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "country": "USA"
}
```

---

### 19. Get User By ID - Invalid ID (Should Fail)

```bash
curl -X GET "$BASE_URL/api/v1/users/invalid-user-id-12345" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (404):** User not found

---

### 20. Get User By ID - Non-Admin (Should Fail)

```bash
curl -X GET "$BASE_URL/api/v1/users/$USER_ID" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (403):** Forbidden - Access Denied

---

### 21. Delete User (Admin Only)

```bash
USER_ID="550e8400-e29b-41d4-a716-446655440000"

curl -X DELETE "$BASE_URL/api/v1/users/$USER_ID" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (204):** No Content

---

### 22. Delete User - Invalid ID (Should Fail)

```bash
curl -X DELETE "$BASE_URL/api/v1/users/invalid-id-12345" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (404):** User not found

---

### 23. Delete User - Non-Admin (Should Fail)

```bash
curl -X DELETE "$BASE_URL/api/v1/users/$USER_ID" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json"
```

**Expected Response (403):** Forbidden - Access Denied

---

## 📊 Complete Testing Workflow Script

```bash
#!/bin/bash

BASE_URL="http://localhost:8085"

echo "========== USER SERVICE API TESTING =========="
echo ""

# 1. Register
echo "1️⃣ Testing REGISTER..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "TestPassword123!",
    "name": "Test",
    "lastName": "User"
  }')

echo "Response: $REGISTER_RESPONSE"
USER_ID=$(echo $REGISTER_RESPONSE | grep -o '"buyerId":"[^"]*' | cut -d'"' -f4)
echo "User ID: $USER_ID"
echo ""

# 2. Login
echo "2️⃣ Testing LOGIN..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/users/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "TestPassword123!"
  }')

echo "Response: $LOGIN_RESPONSE"
ACCESS_TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
REFRESH_TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"refreshToken":"[^"]*' | cut -d'"' -f4)
echo "Access Token: ${ACCESS_TOKEN:0:50}..."
echo "Refresh Token: ${REFRESH_TOKEN:0:50}..."
echo ""

# 3. Get My Profile
echo "3️⃣ Testing GET MY PROFILE..."
curl -s -X GET "$BASE_URL/api/v1/users/me" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" | jq '.'
echo ""

# 4. Update My Profile
echo "4️⃣ Testing UPDATE MY PROFILE..."
curl -s -X PUT "$BASE_URL/api/v1/users/me" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "city": "Ho Chi Minh City",
    "country": "Vietnam"
  }' | jq '.'
echo ""

# 5. Get Updated Profile
echo "5️⃣ Testing GET MY PROFILE (after update)..."
curl -s -X GET "$BASE_URL/api/v1/users/me" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" | jq '.'
echo ""

# 6. Refresh Token
echo "6️⃣ Testing REFRESH TOKEN..."
REFRESH_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/users/refresh?refreshToken=$REFRESH_TOKEN" \
  -H "Content-Type: application/json")
echo "Response: $REFRESH_RESPONSE" | jq '.'
NEW_ACCESS_TOKEN=$(echo $REFRESH_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
echo "New Access Token: ${NEW_ACCESS_TOKEN:0:50}..."
echo ""

# 7. Logout
echo "7️⃣ Testing LOGOUT..."
curl -s -X POST "$BASE_URL/api/v1/users/logout?refreshToken=$REFRESH_TOKEN" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json"
echo ""
echo "✅ Testing completed!"
```

---

## 🧪 Error Response Examples

### 400 - Bad Request (Invalid Email)

```
HTTP/1.1 400 Bad Request
{
  "error": "Email không hợp lệ"
}
```

### 401 - Unauthorized

```
HTTP/1.1 401 Unauthorized
{
  "error": "Unauthorized"
}
```

### 403 - Forbidden (No Admin Role)

```
HTTP/1.1 403 Forbidden
{
  "error": "Access Denied"
}
```

### 404 - Not Found

```
HTTP/1.1 404 Not Found
{
  "error": "User not found"
}
```

### 409 - Conflict (Duplicate Email)

```
HTTP/1.1 409 Conflict
{
  "error": "Email đã được đăng ký"
}
```

### 500 - Internal Server Error

```
HTTP/1.1 500 Internal Server Error
{
  "error": "Internal server error"
}
```

---

## 📋 Testing Checklist

- [ ] Register new user
- [ ] Register with duplicate email (should fail)
- [ ] Register with invalid email (should fail)
- [ ] Register with short password (should fail)
- [ ] Login with correct credentials
- [ ] Login with wrong password (should fail)
- [ ] Login with non-existent email (should fail)
- [ ] Get my profile (with token)
- [ ] Get my profile (without token - should fail)
- [ ] Update my profile (full update)
- [ ] Update my profile (partial update)
- [ ] Update my profile (without token - should fail)
- [ ] Refresh token
- [ ] Get all users (admin only)
- [ ] Get all users (non-admin - should fail)
- [ ] Get user by ID (admin only)
- [ ] Get user by ID (invalid ID - should fail)
- [ ] Delete user (admin only)
- [ ] Delete user (non-admin - should fail)
- [ ] Logout

---

## 💡 Tips

1. **Save tokens for reuse**: After login, save accessToken and refreshToken
2. **Use jq for pretty output**: Add `| jq '.'` to format JSON responses
3. **Import Postman Collection**: Use the JSON file for automated testing
4. **Test error cases**: Always test negative scenarios (wrong password, invalid token, etc.)
5. **Check token expiry**: accessToken typically expires in 5 minutes
6. **Use environment variables**: Store tokens in shell variables for convenience
