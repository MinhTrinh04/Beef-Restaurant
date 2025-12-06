package com.eshop.UserService.Event;

import com.eshop.UserService.Model.UserProfile;
import com.eshop.UserService.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Event Listener để sync thông tin user giữa Keycloak và UserService Database
 * Đảm bảo tính nhất quán dữ liệu, đặc biệt là email cho việc gửi notification
 */
@Component
@Slf4j
public class UserSyncEventListener {

    @Autowired
    private UserRepository userRepository;

    /**
     * Xử lý khi user được tạo - đồng bộ email
     */
    @TransactionalEventListener
    public void onUserCreated(UserCreatedEvent event) {
        log.info("UserCreatedEvent received for email: {}", event.getEmail());

        try {
            String email = event.getEmail();
            // Kiểm tra xem user đã tồn tại chưa (email là ID)
            if (email != null && userRepository.existsById(email)) {
                log.info("User {} already exists, skipping creation", email);
                return;
            }

            // Tạo user profile mới
            UserProfile userProfile = new UserProfile();
            userProfile.setEmail(email);
            userProfile.setKeycloakUserId(event.getKeycloakUserId());
            userProfile.setFirstName(event.getFirstName());
            userProfile.setLastName(event.getLastName());

            userRepository.save(userProfile);
            log.info("User profile created from Keycloak event: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Error creating user profile from event: {}", e.getMessage());
        }
    }

    /**
     * Xử lý khi user được cập nhật
     */
    @TransactionalEventListener
    public void onUserUpdated(UserUpdatedEvent event) {
        log.info("UserUpdatedEvent received for email: {}", event.getEmail());

        try {
            String email = event.getEmail();
            if (email != null) {
                userRepository.findById(email).ifPresentOrElse(
                        userProfile -> {
                            // Cập nhật thông tin từ Keycloak
                            if (event.getFirstName() != null) {
                                userProfile.setFirstName(event.getFirstName());
                            }
                            if (event.getLastName() != null) {
                                userProfile.setLastName(event.getLastName());
                            }

                            if (userProfile != null) {
                                userRepository.save(userProfile);
                                log.info("User profile updated from Keycloak event: {}", event.getEmail());
                            }
                        },
                        () -> log.warn("User not found for update event: {}", event.getEmail()));
            } else {
                log.warn("Email is null for update event");
            }

        } catch (Exception e) {
            log.error("Error updating user profile from event: {}", e.getMessage());
        }
    }

    /**
     * Xử lý khi user bị xoá
     */
    @TransactionalEventListener
    public void onUserDeleted(UserDeletedEvent event) {
        log.info("UserDeletedEvent received for email: {}", event.getEmail());

        try {
            String email = event.getEmail();
            if (email != null && userRepository.existsById(email)) {
                userRepository.deleteById(email);
                log.info("User profile deleted: {}", email);
            }

        } catch (Exception e) {
            log.error("Error deleting user profile from event: {}", e.getMessage());
        }
    }
}
