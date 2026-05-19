package com.realestate.service;

import com.realestate.dto.ForgotPasswordDto;
import com.realestate.dto.UserLoginDto;
import com.realestate.dto.UserRegistrationDto;
import com.realestate.dto.UserResponseDto;
import com.realestate.dto.UserUpdateDto;
import com.realestate.entity.User;
import com.realestate.exception.UserAlreadyExistsException;
import com.realestate.exception.UserNotFoundException;
import com.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Register a new user
     * @param registrationDto the registration data
     * @return UserResponseDto with created user data
     */
    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        // Check if user already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + registrationDto.getEmail() + " already exists");
        }

        // Create new user
        User user = new User(
                registrationDto.getEmail(),
                registrationDto.getPassword(), // In production, hash the password
                registrationDto.getFirstName(),
                registrationDto.getLastName()
        );

        // Set optional fields
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setAddress(registrationDto.getAddress());
        user.setCity(registrationDto.getCity());
        user.setState(registrationDto.getState());
        user.setZipCode(registrationDto.getZipCode());

        // Save user to database
        User savedUser = userRepository.save(user);

        // Return response DTO (without password)
        return convertToResponseDto(savedUser);
    }

    /**
     * Login user with email and password
     * @param loginDto the login credentials
     * @return UserResponseDto if login successful
     */
    public UserResponseDto loginUser(UserLoginDto loginDto) {
        // Find user by email
        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with email: " + loginDto.getEmail());
        }

        // Check password (in production, use bcrypt comparison)
        if (!user.get().getPassword().equals(loginDto.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Check if user is active
        if (!user.get().getIsActive()) {
            throw new RuntimeException("User account is inactive");
        }

        return convertToResponseDto(user.get());
    }

    /**
     * Get user profile by ID
     * @param userId the user ID
     * @return UserResponseDto with user data
     */
    public UserResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return convertToResponseDto(user);
    }

    /**
     * Update user profile
     * @param userId the user ID
     * @param updateDto the updated user data
     * @return UserResponseDto with updated user data
     */
    public UserResponseDto updateUserProfile(Long userId, UserUpdateDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Update user fields
        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());
        user.setPhoneNumber(updateDto.getPhoneNumber());
        user.setAddress(updateDto.getAddress());
        user.setCity(updateDto.getCity());
        user.setState(updateDto.getState());
        user.setZipCode(updateDto.getZipCode());

        // Save updated user
        User updatedUser = userRepository.save(user);

        return convertToResponseDto(updatedUser);
    }

    /**
     * Handle forgot password request (email simulation)
     * @param forgotPasswordDto the forgot password request
     * @return message confirming the request
     */
    public String handleForgotPassword(ForgotPasswordDto forgotPasswordDto) {
        Optional<User> user = userRepository.findByEmail(forgotPasswordDto.getEmail());

        if (user.isEmpty()) {
            // Don't reveal if email exists or not for security
            return "If an account with that email exists, a password reset link has been sent.";
        }

        // In production, generate token and send email
        // For now, just simulate the process
        String resetToken = generateResetToken(user.get().getId());
        System.out.println("Reset token for " + user.get().getEmail() + ": " + resetToken);

        return "If an account with that email exists, a password reset link has been sent.";
    }

    /**
     * Reset password
     * @param userId the user ID
     * @param newPassword the new password
     * @return UserResponseDto with updated user data
     */
    public UserResponseDto resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // In production, hash the password
        user.setPassword(newPassword);

        User updatedUser = userRepository.save(user);
        return convertToResponseDto(updatedUser);
    }

    /**
     * Get all users
     * @return List of UserResponseDto
     */
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Delete user by ID
     * @param userId the user ID to delete
     */
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    /**
     * Convert User entity to UserResponseDto
     * @param user the user entity
     * @return UserResponseDto
     */
    private UserResponseDto convertToResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getZipCode(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getIsActive()
        );
    }

    /**
     * Generate a reset token (simple implementation)
     * @param userId the user ID
     * @return the reset token
     */
    private String generateResetToken(Long userId) {
        return "RESET_" + userId + "_" + System.currentTimeMillis();
    }
}
