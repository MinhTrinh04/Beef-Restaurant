package com.eshop.UserService.Controller;

import com.eshop.UserService.DTO.LoginRequest;
import com.eshop.UserService.DTO.LoginResponse;
import com.eshop.UserService.DTO.RegisterRequest;
import com.eshop.UserService.DTO.UserDTO;
import com.eshop.UserService.Model.UserProfile;
import com.eshop.UserService.Repository.UserRepository;
import com.eshop.UserService.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserRepository userProfileRepository;
    private UserService userService;

    // ==================== Authentication Endpoints ====================

    /**
     * Đăng ký tài khoản mới
     * POST /api/v1/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserDTO newUser = userService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Đăng nhập
     * POST /api/v1/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse loginResponse = userService.login(request);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Đăng xuất
     * POST /api/v1/users/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        try {
            userService.logout(refreshToken);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Refresh token
     * POST /api/v1/users/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestParam String refreshToken) {
        try {
            LoginResponse newTokens = userService.refreshToken(refreshToken);
            return ResponseEntity.ok(newTokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // ==================== User Profile Endpoints ====================

    /**
     * Lấy thông tin profile người dùng hiện tại (require authentication)
     * GET /api/v1/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfile> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        try {
            String keycloakId = jwt.getSubject();
            return userProfileRepository.findByKeycloakId(keycloakId)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Hồ sơ chưa tồn tại. Vui lòng tạo mới."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Cập nhật profile của user hiện tại
     * PUT /api/v1/users/me
     */
    @PutMapping("/me")
    public ResponseEntity<UserProfile> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserProfile updatedProfile) {

        try {
            String keycloakId = jwt.getSubject();

            return userProfileRepository.findByKeycloakId(keycloakId)
                    .map(existingProfile -> {
                        existingProfile.setName(updatedProfile.getName());
                        existingProfile.setLastName(updatedProfile.getLastName());
                        existingProfile.setStreet(updatedProfile.getStreet());
                        existingProfile.setCity(updatedProfile.getCity());
                        existingProfile.setState(updatedProfile.getState());
                        existingProfile.setCountry(updatedProfile.getCountry());

                        return ResponseEntity.ok(userProfileRepository.save(existingProfile));
                    })
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Không tìm thấy hồ sơ để cập nhật"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ==================== Admin Endpoints ====================

    /**
     * Lấy danh sách tất cả users (chỉ ADMIN)
     * GET /api/v1/users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProfile>> getAllUsers() {
        return ResponseEntity.ok(userProfileRepository.findAll());
    }

    /**
     * Lấy thông tin user theo buyerId (chỉ ADMIN)
     * GET /api/v1/users/{userId}
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfile> getUserById(@PathVariable String userId) {
        return userProfileRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));
    }

    /**
     * Xóa user (chỉ ADMIN)
     * DELETE /api/v1/users/{userId}
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userProfileRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}