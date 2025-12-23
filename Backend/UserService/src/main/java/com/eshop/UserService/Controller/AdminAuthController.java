package com.eshop.UserService.Controller;

import com.eshop.UserService.DTO.*;
import com.eshop.UserService.Model.UserProfile;
import com.eshop.UserService.Repository.UserRepository;
import com.eshop.UserService.Service.KeycloakService;
import com.eshop.UserService.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@Slf4j
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" }, allowCredentials = "true")
public class AdminAuthController {

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Admin Login - Chỉ cho phép users có role ADMIN
     * POST /api/v1/users/admin/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResponse>> adminLogin(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        try {
            log.info("Admin login request for email: {}", request.getEmail());

            // 1. Gọi Keycloak để lấy tokens
            Map<String, Object> tokenResponse = keycloakService.login(
                    request.getEmail(),
                    request.getPassword());

            String accessToken = (String) tokenResponse.get("accessToken");
            String refreshToken = (String) tokenResponse.get("refreshToken");
            Long expiresIn = (Long) tokenResponse.get("expiresIn");

            // 2. Set refresh token vào HttpOnly cookie
            Cookie refreshCookie = new Cookie("admin_refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false); // Set true trong production với HTTPS
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            refreshCookie.setAttribute("SameSite", "Lax");
            response.addCookie(refreshCookie);

            // 3. Lấy profile từ UserService DB (auto-sync nếu chưa tồn tại)
            String email = request.getEmail();
            Optional<UserProfile> userProfileOpt = userRepository.findById(email);

            UserProfile userProfile;
            if (userProfileOpt.isPresent()) {
                userProfile = userProfileOpt.get();
            } else {
                // Nếu profile chưa tồn tại, tạo mới từ thông tin Keycloak (giống user login)
                Map<String, Object> keycloakUserInfo = keycloakService.getUserProfile(accessToken);

                userProfile = new UserProfile();
                userProfile.setEmail(email);
                userProfile.setKeycloakUserId((String) keycloakUserInfo.get("keycloakUserId"));
                userProfile.setFirstName((String) keycloakUserInfo.get("firstName"));
                userProfile.setLastName((String) keycloakUserInfo.get("lastName"));

                userProfile = userRepository.save(userProfile);
                log.info("Admin profile created during login for email: {}", email);
            }

            // 4. Trả về access token cho frontend (lưu trong memory)
            AdminLoginResponse loginResponse = AdminLoginResponse.builder()
                    .accessToken(accessToken)
                    .expiresIn(expiresIn)
                    .tokenType("Bearer")
                    .userProfile(mapToDTO(userProfile))
                    .build();

            log.info("Admin login successful for: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.ok(loginResponse, "Đăng nhập admin thành công"));

        } catch (Exception e) {
            log.error("Admin login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Đăng nhập thất bại", "ADMIN_LOGIN_FAILED"));
        }
    }

    /**
     * Admin Refresh - Lấy access token mới từ refresh token cookie
     * POST /api/v1/users/admin/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshResponse>> adminRefresh(
            @CookieValue(name = "admin_refresh_token", required = false) String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Refresh token không tồn tại", "NO_REFRESH_TOKEN"));
        }

        try {
            log.info("Admin refresh token request");

            // Gọi Keycloak refresh endpoint
            Map<String, Object> tokenResponse = keycloakService.refreshToken(refreshToken);

            String newAccessToken = (String) tokenResponse.get("accessToken");
            Long expiresIn = (Long) tokenResponse.get("expiresIn");

            RefreshResponse refreshResponse = RefreshResponse.builder()
                    .accessToken(newAccessToken)
                    .expiresIn(expiresIn)
                    .tokenType("Bearer")
                    .build();

            log.info("Admin token refreshed successfully");
            return ResponseEntity.ok(ApiResponse.ok(refreshResponse, "Token đã được làm mới"));

        } catch (Exception e) {
            log.error("Admin refresh failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Không thể làm mới token", "REFRESH_FAILED"));
        }
    }

    /**
     * Admin Logout - Xóa refresh token cookie
     * POST /api/v1/users/admin/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> adminLogout(
            @CookieValue(name = "admin_refresh_token", required = false) String refreshToken,
            HttpServletResponse response) {

        try {
            log.info("Admin logout request");

            // 1. Revoke token ở Keycloak (nếu có)
            if (refreshToken != null) {
                keycloakService.logout(refreshToken);
            }

            // 2. Xóa cookie
            Cookie refreshCookie = new Cookie("admin_refresh_token", null);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(0); // Xóa ngay
            response.addCookie(refreshCookie);

            log.info("Admin logout successful");
            return ResponseEntity.ok(ApiResponse.ok(null, "Đăng xuất thành công"));

        } catch (Exception e) {
            log.error("Admin logout failed: {}", e.getMessage());
            // Vẫn trả về success vì đã xóa cookie
            return ResponseEntity.ok(ApiResponse.ok(null, "Đăng xuất thành công"));
        }
    }

    /**
     * Get current admin user - Yêu cầu role ADMIN
     * GET /api/v1/users/admin/auth/me
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getCurrentAdmin(
            @RequestHeader("Authorization") String authHeader) {

        try {
            // Extract email from JWT (đã được validate bởi Spring Security)
            // Hoặc có thể dùng @AuthenticationPrincipal Jwt jwt
            String token = authHeader.replace("Bearer ", "");
            Map<String, Object> userInfo = keycloakService.getUserProfile(token);
            String email = (String) userInfo.get("email");

            UserProfileDTO profile = userService.getMyProfile(email);

            return ResponseEntity.ok(ApiResponse.ok(profile, "Lấy thông tin admin thành công"));

        } catch (Exception e) {
            log.error("Get admin profile failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Không thể lấy thông tin", "GET_PROFILE_FAILED"));
        }
    }

    /**
     * Map UserProfile entity sang DTO
     */
    private UserProfileDTO mapToDTO(UserProfile userProfile) {
        return UserProfileDTO.builder()
                .email(userProfile.getEmail())
                .keycloakUserId(userProfile.getKeycloakUserId())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .phoneNumber(userProfile.getPhoneNumber())
                .createdAt(userProfile.getCreatedAt())
                .updatedAt(userProfile.getUpdatedAt())
                .build();
    }
}
