package com.eshop.UserService.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response cho admin login
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginResponse {
    private String accessToken;
    private Long expiresIn;
    private String tokenType;
    private UserProfileDTO userProfile;
}
