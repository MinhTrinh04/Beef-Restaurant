package com.eshop.UserService.Controller;

import com.eshop.UserService.DTO.*;
import com.eshop.UserService.Model.UserProfile;
import com.eshop.UserService.Repository.UserRepository;
import com.eshop.UserService.Service.KeycloakService;
import com.eshop.UserService.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserRepository userRepository;

    // ==================== AUTH ENDPOINTS ====================

    /**
     * Đăng ký người dùng mới
     * POST /api/v1/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserProfileDTO>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received for email: {}", request.getEmail());

        UserProfileDTO userProfile = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(userProfile, "Đăng ký thành công"));
    }

    /**
     * Đăng nhập và lấy SSO token
     * POST /api/v1/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received for email: {}", request.getEmail());

        LoginResponse loginResponse = userService.login(request);

        return ResponseEntity.ok(ApiResponse.ok(loginResponse, "Đăng nhập thành công"));
    }

    /**
     * Refresh access token
     * POST /api/v1/users/refresh-token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            log.info("Refresh token request received");

            Map<String, Object> tokenResponse = keycloakService.refreshToken(request.getRefreshToken());

            LoginResponse loginResponse = LoginResponse.builder()
                    .accessToken((String) tokenResponse.get("accessToken"))
                    .refreshToken((String) tokenResponse.get("refreshToken"))
                    .expiresIn((Long) tokenResponse.get("expiresIn"))
                    .tokenType((String) tokenResponse.get("tokenType"))
                    .build();

            return ResponseEntity.ok(ApiResponse.ok(loginResponse, "Token được cập nhật thành công"));

        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Không thể cập nhật token", "TOKEN_REFRESH_FAILED"));
        }
    }

    /**
     * Đăng xuất
     * POST /api/v1/users/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            log.info("Logout request received");

            keycloakService.logout(request.getRefreshToken());

            return ResponseEntity.ok(ApiResponse.ok(null, "Đăng xuất thành công"));

        } catch (Exception e) {
            log.error("Error logging out: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Đăng xuất thất bại", "LOGOUT_FAILED"));
        }
    }

    /**
     * Kiểm tra email có tồn tại
     * GET /api/v1/users/check-email?email=xxx@xxx.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkEmailExists(
            @RequestParam String email) {
        log.info("Check email request for: {}", email);

        boolean exists = userService.emailExists(email);

        return ResponseEntity.ok(ApiResponse.ok(
                Map.of("exists", exists),
                "Kiểm tra email thành công"));
    }

    // ==================== USER PROFILE ENDPOINTS ====================

    /**
     * Lấy hồ sơ của chính mình (authenticated user)
     * GET /api/v1/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        log.info("Fetching profile for email: {}", email);

        UserProfileDTO profile = userService.getMyProfile(email);

        return ResponseEntity.ok(ApiResponse.ok(profile, "Lấy hồ sơ thành công"));
    }

    /**
     * Cập nhật hồ sơ của chính mình
     * PUT /api/v1/users/me
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDTO>> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserProfile updatedProfile) {

        String email = jwt.getClaimAsString("email");
        log.info("Updating profile for email: {}", email);

        UserProfileDTO updated = userService.updateProfile(email, updatedProfile);

        return ResponseEntity.ok(ApiResponse.ok(updated, "Cập nhật hồ sơ thành công"));
    }

    // ==================== ADMIN ENDPOINTS ====================

    /**
     * Lấy tất cả người dùng (chỉ cho admin)
     * GET /api/v1/users/admin/all
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserProfileDTO>>> getAllUsers() {
        log.info("Admin fetching all users");

        List<UserProfileDTO> users = userRepository.findAll().stream()
                .map(profile -> UserProfileDTO.builder()
                        .email(profile.getEmail())
                        .keycloakUserId(profile.getKeycloakUserId())
                        .firstName(profile.getFirstName())
                        .lastName(profile.getLastName())
                        .phoneNumber(profile.getPhoneNumber())
                        .createdAt(profile.getCreatedAt())
                        .updatedAt(profile.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.ok(users, "Lấy danh sách người dùng thành công"));
    }

    /**
     * Lấy hồ sơ bằng email (chỉ cho admin)
     * GET /api/v1/users/admin/by-email?email=xxx@xxx.com
     */
    @GetMapping("/admin/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getUserByEmail(@RequestParam String email) {
        log.info("Admin fetching profile for email: {}", email);

        UserProfileDTO profile = userService.getMyProfile(email);

        return ResponseEntity.ok(ApiResponse.ok(profile, "Lấy hồ sơ thành công"));
    }

    /**
     * Xoá người dùng (chỉ cho admin)
     * DELETE /api/v1/users/admin/{email}
     */
    @DeleteMapping("/admin/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String email) {
        log.info("Admin deleting user: {}", email);

        if (!userRepository.existsById(email != null ? email : "")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }

        if (email != null) {
            userRepository.deleteById(email);
        }

        return ResponseEntity.ok(ApiResponse.ok(null, "Xoá người dùng thành công"));
    }
}