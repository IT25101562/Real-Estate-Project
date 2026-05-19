package com.realestate.controller;

import com.realestate.dto.ForgotPasswordDto;
import com.realestate.dto.UserLoginDto;
import com.realestate.dto.UserRegistrationDto;
import com.realestate.dto.UserResponseDto;
import com.realestate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for authentication endpoints
 * Handles user registration, login, and password management
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user
     * POST /api/auth/register
     * @param registrationDto the registration data
     * @return ResponseEntity with user data and status 201
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        UserResponseDto userResponseDto = userService.registerUser(registrationDto);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User registered successfully");
        response.put("data", userResponseDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Login user
     * POST /api/auth/login
     * @param loginDto the login credentials
     * @return ResponseEntity with user data and status 200
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginDto loginDto) {
        UserResponseDto userResponseDto = userService.loginUser(loginDto);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("data", userResponseDto);
        // In production, generate and return JWT token here

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Handle forgot password request
     * POST /api/auth/forgot-password
     * @param forgotPasswordDto the email address
     * @return ResponseEntity with success message
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        String message = userService.handleForgotPassword(forgotPasswordDto);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Health check endpoint
     * GET /api/auth/health
     * @return ResponseEntity with status message
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Auth service is running");
        response.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
