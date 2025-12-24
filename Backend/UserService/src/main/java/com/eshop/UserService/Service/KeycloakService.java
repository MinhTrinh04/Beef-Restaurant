package com.eshop.UserService.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KeycloakService {

    @Value("${keycloak.server-url:http://localhost:8180}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:beef}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin-username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin-password:admin}")
    private String adminPassword;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private static final long TOKEN_CACHE_DURATION = 55 * 60 * 1000; // 55 phút
    private String cachedAdminToken;
    private long tokenCachedTime;

    /**
     * Đăng ký người dùng mới trên Keycloak
     * Email bắt buộc phải có để sync với UserService database
     */
    public String registerUser(String email, String password, String firstName, String lastName) throws Exception {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không được để trống");
        }

        try {
            String adminToken = getAdminToken();

            // Tạo user object
            JsonObject userJson = new JsonObject();
            userJson.addProperty("username", email);
            userJson.addProperty("email", email);
            userJson.addProperty("firstName", firstName);
            userJson.addProperty("lastName", lastName != null ? lastName : "");
            userJson.addProperty("emailVerified", true); // ✅ Set true to bypass email verification requirement
            userJson.addProperty("enabled", true); // ✅ Ensure user is enabled

            // Log payload for debugging
            log.info("Sending Create User Request to Keycloak: {}", userJson.toString());

            // Set password
            JsonArray credentials = new JsonArray();
            JsonObject credential = new JsonObject();
            credential.addProperty("type", "password");
            credential.addProperty("value", password);
            credential.addProperty("temporary", false);
            credentials.add(credential);
            userJson.add("credentials", credentials);

            // Gửi request tạo user
            String url = keycloakServerUrl + "/admin/realms/" + realm + "/users";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + adminToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(userJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                String location = response.headers().firstValue("Location").orElse("");
                String keycloakUserId = location.substring(location.lastIndexOf("/") + 1);
                log.info("User registered successfully on Keycloak: {}", email);

                // Trigger email verification
                // sendVerificationEmail(keycloakUserId);
                // log.info("Verification email sent to: {}", email);

                return keycloakUserId;
            } else if (response.statusCode() == 409) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email đã được đăng ký");
            } else {
                log.error("Failed to register user on Keycloak: {}", response.body());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Đăng ký thất bại: " + response.body());
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi đăng ký: " + e.getMessage());
        }
    }

    /**
     * Đăng nhập và lấy token SSO từ Keycloak
     */
    public Map<String, Object> login(String email, String password) throws Exception {
        try {
            String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            String body = "grant_type=password" +
                    "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                    "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                    "&username=" + URLEncoder.encode(email, StandardCharsets.UTF_8) +
                    "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8) +
                    "&scope=openid+profile+email";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                String accessToken = jsonResponse.get("access_token").getAsString();

                // Check if email is verified
                if (!isEmailVerified(accessToken)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "Email chưa được xác thực. Vui lòng kiểm tra email và xác thực tài khoản trước khi đăng nhập.");
                }

                Map<String, Object> result = new HashMap<>();
                result.put("accessToken", accessToken);
                result.put("refreshToken", jsonResponse.get("refresh_token").getAsString());
                result.put("expiresIn", jsonResponse.get("expires_in").getAsLong());
                result.put("tokenType", jsonResponse.get("token_type").getAsString());
                log.info("User logged in successfully: {}", email);
                return result;
            } else if (response.statusCode() == 401) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không chính xác");
            } else {
                log.error("Login failed: {}", response.body());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Đăng nhập thất bại");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi đăng nhập: " + e.getMessage());
        }
    }

    /**
     * Logout - invalidate refresh token
     */
    public void logout(String refreshToken) throws Exception {
        String logoutUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

        String body = "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                "&refresh_token=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(logoutUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Refresh token
     */
    public Map<String, Object> refreshToken(String refreshToken) throws Exception {
        String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        String body = "grant_type=refresh_token" +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                "&refresh_token=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", jsonResponse.get("access_token").getAsString());
            result.put("refreshToken", jsonResponse.get("refresh_token").getAsString());
            result.put("expiresIn", jsonResponse.get("expires_in").getAsLong());
            result.put("tokenType", jsonResponse.get("token_type").getAsString());
            return result;
        } else {
            throw new Exception("Token refresh failed: " + response.body());
        }
    }

    /**
     * Lấy thông tin user từ Keycloak bằng access token
     */
    public Map<String, Object> getUserProfile(String accessToken) throws Exception {
        try {
            String userInfoUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(userInfoUrl))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject userInfo = gson.fromJson(response.body(), JsonObject.class);
                Map<String, Object> result = new HashMap<>();
                result.put("keycloakUserId", userInfo.get("sub").getAsString());
                result.put("email", userInfo.get("email").getAsString());
                // given_name và family_name có thể null nếu chưa set trong Keycloak
                result.put("firstName",
                        userInfo.get("given_name") != null ? userInfo.get("given_name").getAsString() : "");
                result.put("lastName",
                        userInfo.get("family_name") != null ? userInfo.get("family_name").getAsString() : "");
                result.put("emailVerified", userInfo.get("email_verified").getAsBoolean());
                return result;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting user profile: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Lấy admin token với caching để giảm số lần gọi API
     */
    private String getAdminToken() throws Exception {
        // Kiểm tra cache
        if (cachedAdminToken != null && (System.currentTimeMillis() - tokenCachedTime) < TOKEN_CACHE_DURATION) {
            return cachedAdminToken;
        }

        try {
            // Admin user is always in 'master' realm
            String tokenUrl = keycloakServerUrl + "/realms/master/protocol/openid-connect/token";

            String body = "grant_type=password" +
                    "&client_id=admin-cli" +
                    "&username=" + URLEncoder.encode(adminUsername, StandardCharsets.UTF_8) +
                    "&password=" + URLEncoder.encode(adminPassword, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                cachedAdminToken = jsonResponse.get("access_token").getAsString();
                tokenCachedTime = System.currentTimeMillis();
                log.debug("Admin token refreshed");
                return cachedAdminToken;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Không thể lấy admin token từ Keycloak");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting admin token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi xác thực admin");
        }
    }

    public void assignUserRole(String keycloakUserId) throws Exception {
        try {
            String adminToken = getAdminToken();

            // 1. Lấy role ID của "USER" role
            String roleUrl = keycloakServerUrl + "/admin/realms/" + realm + "/roles/USER";
            HttpRequest getRoleRequest = HttpRequest.newBuilder()
                    .uri(URI.create(roleUrl))
                    .header("Authorization", "Bearer " + adminToken)
                    .GET()
                    .build();

            HttpResponse<String> roleResponse = httpClient.send(getRoleRequest, HttpResponse.BodyHandlers.ofString());

            if (roleResponse.statusCode() != 200) {
                log.warn("USER role not found in Keycloak, skipping role assignment");
                return;
            }

            JsonObject roleJson = gson.fromJson(roleResponse.body(), JsonObject.class);

            // 2. Gán role cho user
            String assignRoleUrl = keycloakServerUrl + "/admin/realms/" + realm +
                    "/users/" + keycloakUserId + "/role-mappings/realm";

            JsonArray rolesArray = new JsonArray();
            rolesArray.add(roleJson);

            HttpRequest assignRoleRequest = HttpRequest.newBuilder()
                    .uri(URI.create(assignRoleUrl))
                    .header("Authorization", "Bearer " + adminToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(rolesArray.toString()))
                    .build();

            HttpResponse<String> assignResponse = httpClient.send(assignRoleRequest,
                    HttpResponse.BodyHandlers.ofString());

            if (assignResponse.statusCode() == 204 || assignResponse.statusCode() == 201) {
                log.info("Role USER assigned to user: {}", keycloakUserId);
            } else {
                log.warn("Failed to assign USER role: {}", assignResponse.body());
            }
        } catch (Exception e) {
            log.error("Error assigning role to user: {}", e.getMessage());
            // Không throw exception - registration should succeed even if role assignment
            // fails
        }
    }

    /**
     * Send verification email to user via Keycloak
     */
    private void sendVerificationEmail(String keycloakUserId) throws Exception {
        try {
            String adminToken = getAdminToken();

            // Keycloak API: PUT /admin/realms/{realm}/users/{id}/send-verify-email
            String url = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + keycloakUserId
                    + "/send-verify-email";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + adminToken)
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204 || response.statusCode() == 200) {
                log.info("Verification email sent successfully for user: {}", keycloakUserId);
            } else {
                log.error("Failed to send verification email: {}", response.body());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Không thể gửi email xác thực. Vui lòng thử lại sau.");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error sending verification email: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi gửi email xác thực: " + e.getMessage());
        }
    }

    /**
     * Resend verification email to user by email address
     */
    public void resendVerificationEmail(String email) throws Exception {
        try {
            String adminToken = getAdminToken();

            // Get user by email
            String searchUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users?email=" +
                    URLEncoder.encode(email, StandardCharsets.UTF_8);

            HttpRequest searchRequest = HttpRequest.newBuilder()
                    .uri(URI.create(searchUrl))
                    .header("Authorization", "Bearer " + adminToken)
                    .GET()
                    .build();

            HttpResponse<String> searchResponse = httpClient.send(searchRequest, HttpResponse.BodyHandlers.ofString());

            if (searchResponse.statusCode() == 200) {
                JsonArray users = gson.fromJson(searchResponse.body(), JsonArray.class);

                if (users.size() == 0) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email không tồn tại trong hệ thống");
                }

                JsonObject user = users.get(0).getAsJsonObject();
                String keycloakUserId = user.get("id").getAsString();
                boolean emailVerified = user.get("emailVerified").getAsBoolean();

                if (emailVerified) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Email đã được xác thực. Bạn có thể đăng nhập ngay.");
                }

                // Send verification email
                sendVerificationEmail(keycloakUserId);
                log.info("Resent verification email to: {}", email);

            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Không thể tìm kiếm người dùng");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error resending verification email: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi gửi lại email xác thực: " + e.getMessage());
        }
    }

    /**
     * Check if user's email is verified by access token
     */
    private boolean isEmailVerified(String accessToken) throws Exception {
        try {
            String userInfoUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(userInfoUrl))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject userInfo = gson.fromJson(response.body(), JsonObject.class);
                return userInfo.has("email_verified") && userInfo.get("email_verified").getAsBoolean();
            } else {
                log.error("Failed to get user info: {}", response.body());
                return false;
            }
        } catch (Exception e) {
            log.error("Error checking email verification: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if email is verified by email address
     */
    public boolean checkEmailVerified(String email) throws Exception {
        try {
            String adminToken = getAdminToken();

            String searchUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users?email=" +
                    URLEncoder.encode(email, StandardCharsets.UTF_8);

            HttpRequest searchRequest = HttpRequest.newBuilder()
                    .uri(URI.create(searchUrl))
                    .header("Authorization", "Bearer " + adminToken)
                    .GET()
                    .build();

            HttpResponse<String> searchResponse = httpClient.send(searchRequest, HttpResponse.BodyHandlers.ofString());

            if (searchResponse.statusCode() == 200) {
                JsonArray users = gson.fromJson(searchResponse.body(), JsonArray.class);

                if (users.size() == 0) {
                    return false;
                }

                JsonObject user = users.get(0).getAsJsonObject();
                return user.get("emailVerified").getAsBoolean();
            }

            return false;
        } catch (Exception e) {
            log.error("Error checking email verification status: {}", e.getMessage());
            return false;
        }
    }
}