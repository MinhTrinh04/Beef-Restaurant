package com.eshop.UserService.Service;

import com.eshop.UserService.DTO.LoginRequest;
import com.eshop.UserService.DTO.LoginResponse;
import com.eshop.UserService.DTO.RegisterRequest;
import com.eshop.UserService.DTO.UserDTO;
import com.eshop.UserService.Model.UserProfile;
import com.eshop.UserService.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private KeycloakService keycloakService;

    /**
     * Đăng ký người dùng mới
     */
    public UserDTO register(RegisterRequest request) throws Exception {
        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email đã được đăng ký");
        }

        // Tạo user trên Keycloak
        String keycloakUserId = keycloakService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getLastName());

        // Lưu thông tin user vào database (tạo profile)
        UserProfile userProfile = new UserProfile();
        userProfile.setBuyerId(UUID.randomUUID().toString()); // Tạo ID cho buyer
        userProfile.setEmail(request.getEmail());
        userProfile.setName(request.getName());
        userProfile.setLastName(request.getLastName());
        userProfile.setKeycloakId(keycloakUserId);

        UserProfile saved = userRepository.save(userProfile);

        return mapToDTO(saved);
    }

    /**
     * Đăng nhập
     */
    public LoginResponse login(LoginRequest request) throws Exception {
        // Gọi Keycloak để verify credentials và lấy token
        Map<String, Object> tokenResponse = keycloakService.login(
                request.getEmail(),
                request.getPassword());

        // Lấy thông tin user từ database
        UserProfile userProfile = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tìm thấy"));

        // Tạo response
        LoginResponse response = new LoginResponse();
        response.setAccessToken((String) tokenResponse.get("accessToken"));
        response.setRefreshToken((String) tokenResponse.get("refreshToken"));
        response.setExpiresIn((Long) tokenResponse.get("expiresIn"));
        response.setTokenType((String) tokenResponse.get("tokenType"));
        response.setUser(mapToDTO(userProfile));

        return response;
    }

    /**
     * Logout
     */
    public void logout(String refreshToken) throws Exception {
        keycloakService.logout(refreshToken);
    }

    /**
     * Refresh token
     */
    public LoginResponse refreshToken(String refreshToken) throws Exception {
        Map<String, Object> tokenResponse = keycloakService.refreshToken(refreshToken);

        LoginResponse response = new LoginResponse();
        response.setAccessToken((String) tokenResponse.get("accessToken"));
        response.setRefreshToken((String) tokenResponse.get("refreshToken"));
        response.setExpiresIn((Long) tokenResponse.get("expiresIn"));
        response.setTokenType((String) tokenResponse.get("tokenType"));

        return response;
    }

    /**
     * Lấy thông tin profile người dùng hiện tại
     */
    public UserDTO getMyProfile(String keycloakId) {
        UserProfile userProfile = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Hồ sơ chưa tồn tại. Vui lòng tạo mới."));
        return mapToDTO(userProfile);
    }

    /**
     * Cập nhật profile người dùng
     */
    public UserDTO updateProfile(String keycloakId, UserDTO updateRequest) {
        UserProfile userProfile = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tìm thấy"));

        if (updateRequest.getName() != null) {
            userProfile.setName(updateRequest.getName());
        }
        if (updateRequest.getLastName() != null) {
            userProfile.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getStreet() != null) {
            userProfile.setStreet(updateRequest.getStreet());
        }
        if (updateRequest.getCity() != null) {
            userProfile.setCity(updateRequest.getCity());
        }
        if (updateRequest.getState() != null) {
            userProfile.setState(updateRequest.getState());
        }
        if (updateRequest.getCountry() != null) {
            userProfile.setCountry(updateRequest.getCountry());
        }

        UserProfile updated = userRepository.save(userProfile);
        return mapToDTO(updated);
    }

    /**
     * Map UserProfile entity to UserDTO
     */
    private UserDTO mapToDTO(UserProfile userProfile) {
        UserDTO dto = new UserDTO();
        dto.setBuyerId(userProfile.getBuyerId());
        dto.setEmail(userProfile.getEmail());
        dto.setName(userProfile.getName());
        dto.setLastName(userProfile.getLastName());
        dto.setStreet(userProfile.getStreet());
        dto.setCity(userProfile.getCity());
        dto.setState(userProfile.getState());
        dto.setCountry(userProfile.getCountry());
        return dto;
    }
}
