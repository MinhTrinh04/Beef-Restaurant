package com.eshop.UserService.Service;

import com.eshop.UserService.DTO.LoginRequest;
import com.eshop.UserService.DTO.LoginResponse;
import com.eshop.UserService.DTO.RegisterRequest;
import com.eshop.UserService.DTO.UserProfileDTO;
import com.eshop.UserService.Model.UserProfile;
import com.eshop.UserService.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeycloakService keycloakService;

    /**
     * Đăng ký người dùng mới
     * 1. Tạo account trên Keycloak (SSO)
     * 2. Tạo profile trên UserService DB (email làm ID)
     */
    public UserProfileDTO register(RegisterRequest request) {
        try {
            // 1. Kiểm tra email đã tồn tại trong UserService DB
            if (userRepository.existsById(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email đã được đăng ký");
            }

            // 2. Đăng ký trên Keycloak
            String keycloakUserId = keycloakService.registerUser(
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName());

            log.info("User registered on Keycloak with ID: {}", keycloakUserId);

            // 2.5 Gán role USER cho user (tự động)
            keycloakService.assignUserRole(keycloakUserId);

            // 3. Tạo profile trong UserService DB
            UserProfile userProfile = new UserProfile();
            userProfile.setEmail(request.getEmail());
            userProfile.setKeycloakUserId(keycloakUserId);
            userProfile.setFirstName(request.getFirstName());
            userProfile.setLastName(request.getLastName());
            userProfile.setPhoneNumber(request.getPhoneNumber());

            UserProfile savedProfile = userRepository.save(userProfile);
            log.info("User profile created in DB for email: {}", request.getEmail());

            return mapToDTO(savedProfile);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Đăng ký thất bại: " + e.getMessage());
        }
    }

    /**
     * Đăng nhập và lấy SSO token
     * 1. Xác thực trên Keycloak
     * 2. Lấy profile từ UserService DB
     * 3. Trả về token + user profile
     */
    public LoginResponse login(LoginRequest request) {
        try {
            // 1. Xác thực trên Keycloak
            Map<String, Object> tokenResponse = keycloakService.login(request.getEmail(), request.getPassword());

            // 2. Lấy profile từ UserService DB (email là key)
            String email = request.getEmail();
            Optional<UserProfile> userProfileOpt = email != null ? userRepository.findById(email) : Optional.empty();

            UserProfile userProfile;
            if (userProfileOpt.isPresent()) {
                userProfile = userProfileOpt.get();
            } else {
                // Nếu profile chưa tồn tại, tạo mới từ thông tin Keycloak
                String accessToken = (String) tokenResponse.get("accessToken");
                Map<String, Object> keycloakUserInfo = keycloakService.getUserProfile(accessToken);

                userProfile = new UserProfile();
                userProfile.setEmail(request.getEmail());
                userProfile.setKeycloakUserId((String) keycloakUserInfo.get("keycloakUserId"));
                userProfile.setFirstName((String) keycloakUserInfo.get("firstName"));
                userProfile.setLastName((String) keycloakUserInfo.get("lastName"));

                userProfile = userRepository.save(userProfile);
                log.info("User profile created during login for email: {}", request.getEmail());
            }

            log.info("User logged in successfully: {}", request.getEmail());

            // 3. Build response
            return LoginResponse.builder()
                    .accessToken((String) tokenResponse.get("accessToken"))
                    .refreshToken((String) tokenResponse.get("refreshToken"))
                    .expiresIn((Long) tokenResponse.get("expiresIn"))
                    .tokenType((String) tokenResponse.get("tokenType"))
                    .userProfile(mapToDTO(userProfile))
                    .build();

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Đăng nhập thất bại: " + e.getMessage());
        }
    }

    /**
     * Lấy profile người dùng hiện tại (email là key)
     */
    public UserProfileDTO getMyProfile(String email) {
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không được rỗng");
        }
        return userRepository.findById(email)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hồ sơ chưa tồn tại"));
    }

    /**
     * Cập nhật profile người dùng (chỉ cập nhật firstName, lastName, phoneNumber)
     */
    public UserProfileDTO updateProfile(String email, UserProfile updateRequest) {
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email không được rỗng");
        }
        UserProfile existingProfile = userRepository.findById(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hồ sơ chưa tồn tại"));

        // Cập nhật các trường được phép thay đổi (không cập nhật email, keycloakUserId)
        if (updateRequest.getFirstName() != null) {
            existingProfile.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            existingProfile.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getPhoneNumber() != null) {
            existingProfile.setPhoneNumber(updateRequest.getPhoneNumber());
        }

        if (existingProfile != null) {
            UserProfile updated = userRepository.save(existingProfile);
            log.info("User profile updated: {}", email);
            return mapToDTO(updated);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể cập nhật hồ sơ");
        }
    }

    /**
     * Kiểm tra email có tồn tại
     */
    public boolean emailExists(String email) {
        return email != null && userRepository.existsById(email);
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
