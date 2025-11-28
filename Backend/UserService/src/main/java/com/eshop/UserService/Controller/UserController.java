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

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userProfileRepository;

    @GetMapping("/me")
    public ResponseEntity<UserProfile> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        return userProfileRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hồ sơ chưa tồn tại. Vui lòng tạo mới."));
    }

    @PostMapping("/profile")
    public ResponseEntity<UserProfile> createMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserProfile newProfile) {

        String userId = jwt.getSubject();

        if (userProfileRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Hồ sơ người dùng đã tồn tại!");
        }

        newProfile.setBuyerId(userId);
        if (newProfile.getName() == null) newProfile.setName(jwt.getClaimAsString("given_name"));
        if (newProfile.getLastName() == null) newProfile.setLastName(jwt.getClaimAsString("family_name"));

        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileRepository.save(newProfile));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfile> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserProfile updatedProfile) {

        String userId = jwt.getSubject();

        return userProfileRepository.findById(userId)
                .map(existingProfile -> {
                    existingProfile.setName(updatedProfile.getName());
                    existingProfile.setLastName(updatedProfile.getLastName());
                    existingProfile.setStreet(updatedProfile.getStreet());
                    existingProfile.setCity(updatedProfile.getCity());
                    existingProfile.setState(updatedProfile.getState());
                    existingProfile.setCountry(updatedProfile.getCountry());

                    return ResponseEntity.ok(userProfileRepository.save(existingProfile));
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ để cập nhật"));
    }


    @GetMapping
    public ResponseEntity<List<UserProfile>> getAllUsers() {
        return ResponseEntity.ok(userProfileRepository.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserById(@PathVariable String userId) {
        return userProfileRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userProfileRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}