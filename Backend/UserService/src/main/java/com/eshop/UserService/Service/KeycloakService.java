package com.eshop.UserService.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class KeycloakService {

    @Value("${keycloak.server-url:http://localhost:8180}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:master}")
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

    /**
     * Đăng ký người dùng mới trên Keycloak
     */
    public String registerUser(String email, String password, String firstName, String lastName) throws Exception {
        // Lấy admin token trước
        String adminToken = getAdminToken();

        // Tạo user object
        JsonObject userJson = new JsonObject();
        userJson.addProperty("username", email);
        userJson.addProperty("email", email);
        userJson.addProperty("firstName", firstName);
        userJson.addProperty("lastName", lastName != null ? lastName : "");
        userJson.addProperty("enabled", true);

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
            // Lấy user ID từ location header
            String location = response.headers().firstValue("Location").orElse("");
            return location.substring(location.lastIndexOf("/") + 1);
        } else {
            throw new Exception("Failed to register user: " + response.body());
        }
    }

    /**
     * Đăng nhập và lấy token
     */
    public Map<String, Object> login(String email, String password) throws Exception {
        String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        String adminToken = getAdminToken();
        String body = "grant_type=password" +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                "&username=" + URLEncoder.encode(email, StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

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
            throw new Exception("Login failed: " + response.body());
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
     * Lấy thông tin user từ Keycloak
     */
    public JsonObject getUserInfo(String keycloakUserId, String adminToken) throws Exception {
        String url = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + keycloakUserId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + adminToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), JsonObject.class);
        } else {
            throw new Exception("Failed to get user info: " + response.body());
        }
    }

    /**
     * Lấy admin token
     */
    private String getAdminToken() throws Exception {
        String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        // Use password grant with admin credentials
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
            return jsonResponse.get("access_token").getAsString();
        } else {
            throw new Exception("Failed to get admin token: " + response.body());
        }
    }
}
