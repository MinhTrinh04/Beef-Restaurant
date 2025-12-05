# Hướng Dẫn Setup Postman Bằng Tay - User Service API

## 📋 Mục Lục

1. [Tạo Workspace & Collection](#1-tạo-workspace--collection)
2. [Setup Environment Variables](#2-setup-environment-variables)
3. [Tạo Requests](#3-tạo-requests)
4. [Testing Workflow](#4-testing-workflow)

---

## 1. Tạo Workspace & Collection

### Bước 1.1: Tạo mới Workspace

```
1. Mở Postman
2. Click "Workspaces" (góc trên trái)
3. Click "Create Workspace"
4. Đặt tên: "Beef Restaurant"
5. Mô tả: "API Testing for Beef Restaurant"
6. Click "Create"
```

### Bước 1.2: Tạo Collection

```
1. Trong Workspace vừa tạo
2. Click "+" (New) hoặc "Create"
3. Chọn "Collection"
4. Đặt tên: "User Service API"
5. Mô tả: "Complete API collection for User Service"
6. Click "Create"
```

### Bước 1.3: Tạo Folders trong Collection

```
1. Hover chuột vào Collection "User Service API"
2. Click "..." (ba chấm)
3. Chọn "Add folder"
4. Tạo 3 folders:
   - Authentication
   - User Profile
   - Admin Operations
```

---

## 2. Setup Environment Variables

### Bước 2.1: Tạo Environment

```
1. Click "Environments" (sidebar trái)
2. Click "+" hoặc "Create Environment"
3. Đặt tên: "Local Development"
4. Click "Create"
```

### Bước 2.2: Add Variables

Trong Environment "Local Development", thêm những variables sau:

| Variable        | Initial Value         | Current Value          |
| --------------- | --------------------- | ---------------------- |
| `base_url`      | http://localhost:8085 | http://localhost:8085  |
| `access_token`  | (để trống)            | (sẽ được tự động fill) |
| `refresh_token` | (để trống)            | (sẽ được tự động fill) |
| `admin_token`   | (để trống)            | (sẽ được tự động fill) |
| `user_id`       | (để trống)            | (sẽ được tự động fill) |
| `user_email`    | john.doe@example.com  | john.doe@example.com   |
| `user_password` | Password123!          | Password123!           |

```
Cách thêm:
1. Scroll xuống "Variables" section
2. Nhập biến trong cột "Variable"
3. Nhập giá trị trong "Initial Value"
4. Click "Save"
```

### Bước 2.3: Chọn Environment

```
1. Góc trên phải, có dropdown
2. Chọn "Local Development"
3. Bây giờ bạn có thể dùng {{base_url}}, {{access_token}}, etc.
```

---

## 3. Tạo Requests

### 🔐 AUTHENTICATION FOLDER

#### Request 3.1: Register New User

```
Tên: "Register New User"
Loại: POST
URL: {{base_url}}/api/v1/users/register

Headers:
  Content-Type: application/json

Body (raw, JSON):
{
  "email": "john.doe@example.com",
  "password": "Password123!",
  "name": "John",
  "lastName": "Doe"
}

Tests (click tab Tests):
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set('user_id', jsonData.buyerId);
    pm.environment.set('user_email', jsonData.email);
    console.log('User ID saved: ' + jsonData.buyerId);
}
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

#### Request 3.2: Login

```
Tên: "Login"
Loại: POST
URL: {{base_url}}/api/v1/users/login

Headers:
  Content-Type: application/json

Body (raw, JSON):
{
  "email": "{{user_email}}",
  "password": "{{user_password}}"
}

Tests (click tab Tests):
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set('access_token', jsonData.accessToken);
    pm.environment.set('refresh_token', jsonData.refreshToken);
    console.log('Tokens saved');
    console.log('Access Token expires in: ' + jsonData.expiresIn + ' seconds');
}
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
    "lastName": "Doe"
  }
}
```

---

#### Request 3.3: Logout

```
Tên: "Logout"
Loại: POST
URL: {{base_url}}/api/v1/users/logout?refreshToken={{refresh_token}}

Headers:
  Authorization: Bearer {{access_token}}
  Content-Type: application/json

Query Params:
  refreshToken: {{refresh_token}}

Tests:
if (pm.response.code === 200) {
    console.log('Logged out successfully');
    pm.environment.set('access_token', '');
    pm.environment.set('refresh_token', '');
}
```

**Expected Response (200):** OK

---

#### Request 3.4: Refresh Token

```
Tên: "Refresh Token"
Loại: POST
URL: {{base_url}}/api/v1/users/refresh?refreshToken={{refresh_token}}

Headers:
  Content-Type: application/json

Query Params:
  refreshToken: {{refresh_token}}

Tests:
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set('access_token', jsonData.accessToken);
    pm.environment.set('refresh_token', jsonData.refreshToken);
    console.log('New access token obtained');
}
```

---

#### Request 3.5: Login - Wrong Password (Error Test)

```
Tên: "Login - Wrong Password"
Loại: POST
URL: {{base_url}}/api/v1/users/login

Headers:
  Content-Type: application/json

Body (raw, JSON):
{
  "email": "john.doe@example.com",
  "password": "WrongPassword123!"
}

Tests:
if (pm.response.code === 401) {
    console.log('✅ Correctly rejected wrong password');
}
```

**Expected Response (401):** Unauthorized

---

### 👤 USER PROFILE FOLDER

#### Request 3.6: Get My Profile

```
Tên: "Get My Profile"
Loại: GET
URL: {{base_url}}/api/v1/users/me

Headers:
  Authorization: Bearer {{access_token}}
  Content-Type: application/json

Tests:
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    console.log('Profile retrieved for: ' + jsonData.email);
}
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

#### Request 3.7: Update My Profile

```
Tên: "Update My Profile"
Loại: PUT
URL: {{base_url}}/api/v1/users/me

Headers:
  Authorization: Bearer {{access_token}}
  Content-Type: application/json

Body (raw, JSON):
{
  "name": "John Updated",
  "lastName": "Doe Updated",
  "street": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "country": "USA"
}

Tests:
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    console.log('Profile updated: ' + jsonData.city + ', ' + jsonData.country);
}
```

---

#### Request 3.8: Update My Profile - Partial

```
Tên: "Update My Profile - Partial"
Loại: PUT
URL: {{base_url}}/api/v1/users/me

Headers:
  Authorization: Bearer {{access_token}}
  Content-Type: application/json

Body (raw, JSON):
{
  "city": "Ho Chi Minh City",
  "country": "Vietnam"
}
```

---

#### Request 3.9: Get My Profile - No Token (Error Test)

```
Tên: "Get My Profile - No Token"
Loại: GET
URL: {{base_url}}/api/v1/users/me

Headers:
  Content-Type: application/json

Tests:
if (pm.response.code === 401) {
    console.log('✅ Correctly requires authentication');
}
```

**Expected Response (401):** Unauthorized

---

### 👮 ADMIN OPERATIONS FOLDER

#### Request 3.10: Get All Users (Admin)

```
Tên: "Get All Users"
Loại: GET
URL: {{base_url}}/api/v1/users

Headers:
  Authorization: Bearer {{admin_token}}
  Content-Type: application/json

Tests:
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    console.log('Total users: ' + jsonData.length);
} else if (pm.response.code === 403) {
    console.log('❌ Access denied - need ADMIN role');
}
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
  }
]
```

---

#### Request 3.11: Get User By ID (Admin)

```
Tên: "Get User By ID"
Loại: GET
URL: {{base_url}}/api/v1/users/{{user_id}}

Headers:
  Authorization: Bearer {{admin_token}}
  Content-Type: application/json

Tests:
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    console.log('User found: ' + jsonData.email);
}
```

---

#### Request 3.12: Delete User (Admin)

```
Tên: "Delete User"
Loại: DELETE
URL: {{base_url}}/api/v1/users/{{user_id}}

Headers:
  Authorization: Bearer {{admin_token}}
  Content-Type: application/json

Tests:
if (pm.response.code === 204) {
    console.log('✅ User deleted successfully');
    pm.environment.set('user_id', '');
}
```

**Expected Response (204):** No Content

---

#### Request 3.13: Get All Users - No Admin (Error Test)

```
Tên: "Get All Users - No Admin Role"
Loại: GET
URL: {{base_url}}/api/v1/users

Headers:
  Authorization: Bearer {{access_token}}
  Content-Type: application/json

Tests:
if (pm.response.code === 403) {
    console.log('✅ Correctly rejected non-admin user');
}
```

**Expected Response (403):** Forbidden

---

## 4. Testing Workflow

### Workflow: Complete User Journey

**Step 1: Register**

```
1. Đi đến Authentication > Register New User
2. Click "Send"
3. Response (201): User tạo thành công
4. {{user_id}} tự động được lưu
```

**Step 2: Login**

```
1. Đi đến Authentication > Login
2. Click "Send"
3. Response (200): Nhận được tokens
4. {{access_token}} và {{refresh_token}} tự động lưu
5. Xem Console (Postman) để confirm
```

**Step 3: Get Profile**

```
1. Đi đến User Profile > Get My Profile
2. Click "Send"
3. Response (200): Xem profile vừa tạo
```

**Step 4: Update Profile**

```
1. Đi đến User Profile > Update My Profile
2. Thay đổi body (street, city, country)
3. Click "Send"
4. Response (200): Confirm được update
```

**Step 5: Verify Update**

```
1. Đi đến User Profile > Get My Profile
2. Click "Send"
3. Response: Thấy các field vừa update
```

**Step 6: Refresh Token** (khi accessToken hết hạn)

```
1. Đi đến Authentication > Refresh Token
2. Click "Send"
3. Response (200): {{access_token}} được refresh
```

**Step 7: Logout**

```
1. Đi đến Authentication > Logout
2. Click "Send"
3. Response (200): Tokens invalidated
4. Tokens reset trong environment
```

---

## 🧪 Testing Checklist

```
Authentication Tests:
  ☐ Register new user (201)
  ☐ Register duplicate email (409)
  ☐ Login success (200)
  ☐ Login wrong password (401)
  ☐ Login non-existent user (401)
  ☐ Refresh token (200)
  ☐ Logout (200)

User Profile Tests:
  ☐ Get my profile (200)
  ☐ Get profile without token (401)
  ☐ Update profile full (200)
  ☐ Update profile partial (200)
  ☐ Update without token (401)

Admin Tests:
  ☐ Get all users as admin (200)
  ☐ Get all users non-admin (403)
  ☐ Get user by ID as admin (200)
  ☐ Delete user as admin (204)
  ☐ Delete user non-admin (403)
```

---

## 💡 Tips & Tricks

### Tip 1: View Saved Environment Variables

```
1. Click "Environments" (sidebar)
2. Click "Local Development"
3. Scroll xuống "Variables"
4. Xem Current Value của tokens
```

### Tip 2: Decode JWT Token

```
1. Login để lấy access_token
2. Copy token
3. Vào https://jwt.io
4. Paste token vào decoder
5. Xem claims (sub = keycloak ID, email, etc.)
```

### Tip 3: Run Requests in Sequence

```
1. Tạo Collection Runner
2. Click "..." > "Run collection"
3. Chọn requests theo thứ tự
4. Click "Run"
5. Postman chạy từng request liên tục
```

### Tip 4: Using Pre-request Scripts

Nếu cần set variables trước khi gửi request:

```javascript
// Click tab "Pre-request Script" trong request
pm.environment.set("current_time", new Date().toISOString());
```

### Tip 5: Save Request Responses

```
1. Send request
2. Click "Save response" (dưới response)
3. Xem lịch sử responses
```

---

## 🔗 Quick Reference

### Common Headers

```
Authorization: Bearer {{access_token}}
Content-Type: application/json
```

### Common Status Codes

| Code | Meaning               |
| ---- | --------------------- |
| 200  | OK                    |
| 201  | Created               |
| 204  | No Content            |
| 400  | Bad Request           |
| 401  | Unauthorized          |
| 403  | Forbidden             |
| 404  | Not Found             |
| 409  | Conflict              |
| 500  | Internal Server Error |

### Common Error Messages

```json
{
  "error": "Email đã được đăng ký", // 409
  "error": "Email không hợp lệ", // 400
  "error": "Unauthorized", // 401
  "error": "Access Denied", // 403
  "error": "User not found" // 404
}
```

---

## 🚀 Troubleshooting

### Problem: "{{access_token}} is empty"

**Solution:**

1. Chắc chắn đã chạy Login request trước
2. Kiểm tra tab "Tests" - có extract token không?
3. Kiểm tra Environment - token có được save không?

### Problem: "401 Unauthorized"

**Solution:**

1. Token có thể hết hạn (5 phút)
2. Refresh token bằng Refresh Token request
3. Hoặc Login lại

### Problem: "403 Forbidden"

**Solution:**

1. User không có ADMIN role
2. Kiểm tra {{admin_token}} có được set không?
3. Dùng {{access_token}} sẽ bị 403

### Problem: "404 User not found"

**Solution:**

1. {{user_id}} có được set không?
2. User có tồn tại không? (kiểm tra Get All Users)
3. User có bị xóa rồi không?

---

## 📚 Useful Resources

- [Postman Documentation](https://learning.postman.com/)
- [JWT Decoder](https://jwt.io/)
- [HTTP Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
