package com.eshop.UserService.Controller;

import com.eshop.UserService.Model.UserProfile;
import com.eshop.UserService.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userProfileRepository;

    @GetMapping("/me")
    public ResponseEntity<UserProfile> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        UserProfile profile = userProfileRepository.findById(userId)
                .orElseGet(() -> createNewProfile(jwt)); // Tạo mới nếu chưa có

        return ResponseEntity.ok(profile);
    }

    /**
     * API 2: Cập nhật profile của người dùng đang đăng nhập (cho frontend).
     * Dữ liệu gửi lên sẽ dựa trên các trường của ApplicationUser.cs
     */
    @PutMapping("/me")
    public ResponseEntity<UserProfile> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserProfile updatedProfile) {

        String userId = jwt.getSubject();

        // Tìm profile hiện có
        return userProfileRepository.findById(userId)
                .map(existingProfile -> {
                    // Cập nhật các trường từ DTO (Data Transfer Object)
                    // Đây là tất cả các trường từ ApplicationUser.cs
                    existingProfile.setName(updatedProfile.getName());
                    existingProfile.setLastName(updatedProfile.getLastName());

                    // Cập nhật địa chỉ
                    existingProfile.setStreet(updatedProfile.getStreet());
                    existingProfile.setCity(updatedProfile.getCity());
                    existingProfile.setState(updatedProfile.getState());
                    existingProfile.setCountry(updatedProfile.getCountry());

                    // Lưu lại vào CSDL
                    UserProfile savedProfile = userProfileRepository.save(existingProfile);
                    return ResponseEntity.ok(savedProfile);
                })
                // Nếu không tìm thấy profile (trường hợp hiếm), trả về 404
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy profile để cập nhật"));
    }

    /**
     * API 3: Lấy profile theo ID (cho các dịch vụ backend, ví dụ OrderingService).
     * Yêu cầu người gọi phải có một Role đặc biệt, ví dụ 'INTERNAL_SERVICE'.
     * Bạn phải cấu hình Role này trong Keycloak và gán nó cho Client Credentials của các service backend.
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('INTERNAL_SERVICE') or hasRole('ADMIN')") // Bảo mật bằng Method Security
    public ResponseEntity<UserProfile> getProfileByUserId(@PathVariable String userId) {

        return userProfileRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy UserProfile cho ID: " + userId));
    }


    // --- Hàm nội bộ (private helper) ---

    /**
     * Tạo một UserProfile mới trong CSDL khi người dùng đăng nhập lần đầu.
     * Nó sẽ điền các thông tin cơ bản từ token Keycloak.
     * * QUAN TRỌNG: Nó cũng điền các giá trị "NOT_SET" cho các trường
     * @NotBlank (tương đương [Required] trong)
     * để tránh lỗi validation khi lưu.
     */
    private UserProfile createNewProfile(Jwt jwt) {
        UserProfile newProfile = new UserProfile();
        newProfile.setBuyerId(jwt.getSubject()); // ID từ Keycloak

        // Lấy các thông tin cơ bản từ token Keycloak (nếu có)
        newProfile.setName(jwt.getClaimAsString("given_name"));
        newProfile.setLastName(jwt.getClaimAsString("family_name"));
        // String email = jwt.getClaimAsString("email"); // Bạn có thể thêm trường email nếu muốn

        // === Điền giá trị mặc định cho các trường @NotBlank ===
        // (Dựa trên ApplicationUser.cs)
        newProfile.setStreet("NOT_SET");
        newProfile.setCity("NOT_SET");
        newProfile.setState("NOT_SET");
        newProfile.setCountry("NOT_SET");

        return userProfileRepository.save(newProfile);
    }
}
