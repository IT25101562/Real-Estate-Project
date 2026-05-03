package com.realestate.admin.model;

import java.time.LocalDateTime;


public abstract class User {

    // ---- Encapsulated Fields (private) ----
    private int userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String fullName;
    private String userType;       // "ADMIN" or "USER"
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ---- Constructors ----

    /** Default constructor */
    public User() {
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /** Parameterized constructor */
    public User(int userId, String username, String password, String email,
                String phone, String fullName, String userType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.fullName = fullName;
        this.userType = userType;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ---- Abstract Methods (Abstraction) ----
    // Subclasses MUST implement these — demonstrates POLYMORPHISM

    /**
     * Returns the role of this user (e.g., "SUPER_ADMIN", "MODERATOR", "USER").
     * Each subclass provides its own implementation.
     */
    public abstract String getRole();

    /**
     * Checks whether this user has a specific permission.
     * Admin users check their permissions list; regular users return false.
     */
    public abstract boolean hasPermission(String permission);

    /**
     * Returns a formatted display string for this user.
     * Different subclasses display different information.
     */
    public abstract String getDisplayInfo();

    // ---- Getters and Setters (Encapsulation) ----

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ---- Common Methods ----

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userType='" + userType + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
