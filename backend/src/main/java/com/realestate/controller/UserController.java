package com.realestate.controller;

import com.realestate.dto.UserRegistrationDto;
import com.realestate.dto.UserResponseDto;
import com.realestate.dto.UserUpdateDto;
import com.realestate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for user profile endpoints
 * Handles user profile retrieval and updates
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get user profile by ID
     * GET /api/users/{userId}
     * @param userId the user ID
     * @return ResponseEntity with user data
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.getUserProfile(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User profile retrieved successfully");
        response.put("data", userResponseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update user profile
     * PUT /api/users/{userId}
     * @param userId the user ID
     * @param registrationDto the updated user data
     * @return ResponseEntity with updated user data
     */
    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDto updateDto) {
        UserResponseDto userResponseDto = userService.updateUserProfile(userId, updateDto);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User profile updated successfully");
        response.put("data", userResponseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Reset password
     * POST /api/users/{userId}/reset-password
     * @param userId the user ID
     * @param passwordRequest the new password
     * @return ResponseEntity with success message
     */
    @PostMapping("/{userId}/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwordRequest) {
        String newPassword = passwordRequest.get("newPassword");

        if (newPassword == null || newPassword.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "New password is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        UserResponseDto userResponseDto = userService.resetPassword(userId, newPassword);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Password reset successfully");
        response.put("data", userResponseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all users
     * GET /api/users
     * @return ResponseEntity with list of users
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Users retrieved successfully");
        response.put("data", users);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete user by ID
     * DELETE /api/users/{userId}
     * @param userId the user ID
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
