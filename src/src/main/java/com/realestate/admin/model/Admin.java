package com.realestate.admin.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Admin class — extends User.
 * Demonstrates: INHERITANCE (Admin extends User)
 * POLYMORPHISM (overrides getRole(), hasPermission(), getDisplayInfo())
 * ENCAPSULATION (private admin-specific fields)
 * ABSTRACTION (admin-only methods)
 */
public class Admin extends User {

    // ---- Admin-specific encapsulated fields ----
    private int adminId;
    private String adminRole; // "SUPER_ADMIN" or "MODERATOR"
    private String department;
    private String permissions; // Comma-separated: "VIEW_USERS,MANAGE_USERS,..."
    private LocalDateTime lastLogin;

    // ---- Constructors ----

    /** Default constructor */
    public Admin() {
        super();
        setUserType("ADMIN");
        this.adminRole = "MODERATOR";
        this.permissions = "VIEW_USERS,VIEW_PROPERTIES";
    }

    /** Parameterized constructor */
    public Admin(int userId, String username, String password, String email, String phone, String fullName,
            String adminRole, String department, String permissions) {
        super(userId, username, password, email, phone, fullName, "ADMIN");
        this.adminRole = adminRole;
        this.department = department;
        this.permissions = permissions;
    }

    /** Full constructor (including adminId) */
    public Admin(int userId, String username, String password, String email, String phone, String fullName, int adminId,
            String adminRole, String department, String permissions, LocalDateTime lastLogin) {
        super(userId, username, password, email, phone, fullName, "ADMIN");
        this.adminId = adminId;
        this.adminRole = adminRole;
        this.department = department;
        this.permissions = permissions;
        this.lastLogin = lastLogin;
    }

    // ============================================================
    // POLYMORPHISM — Override abstract methods from User
    // ============================================================

    /**
     * Returns the specific admin role (SUPER_ADMIN or MODERATOR).
     * This overrides the abstract method in User — Polymorphism.
     */
    @Override
    public String getRole() {
        return this.adminRole;
    }

    /**
     * Checks if this admin has a specific permission.
     * SUPER_ADMIN has all permissions; MODERATOR checks the list.
     * Overrides abstract method in User — Polymorphism.
     */
    @Override
    public boolean hasPermission(String permission) {
        if ("SUPER_ADMIN".equals(this.adminRole)) {
            return true; // Super admins have all permissions
        }
        if (this.permissions == null || this.permissions.isEmpty()) {
            return false;
        }
        List<String> permList = Arrays.asList(this.permissions.split(","));
        return permList.contains(permission.trim());
    }

    /**
     * Returns formatted display info for admins.
     * Different from RegularUser's display — Polymorphism.
     */
    @Override
    public String getDisplayInfo() {
        return String.format("[ADMIN] %s | Role: %s | Dept: %s | Email: %s",
                getFullName(), adminRole, department, getEmail());
    }

    // ============================================================
    // ABSTRACTION — Admin-only methods (not available in User)
    // ============================================================

    /** Check if this admin can manage other users */
    public boolean canManageUsers() {
        return hasPermission("MANAGE_USERS");
    }

    /** Check if this admin can manage property listings */
    public boolean canManageProperties() {
        return hasPermission("MANAGE_PROPERTIES");
    }

    /** Check if this admin can view system reports */
    public boolean canViewReports() {
        return hasPermission("VIEW_REPORTS");
    }

    /** Check if this admin can manage other admins */
    public boolean canManageAdmins() {
        return hasPermission("MANAGE_ADMINS");
    }

    /** Get the list of permissions as a List */
    public List<String> getPermissionList() {
        if (this.permissions == null || this.permissions.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(this.permissions.split(","));
    }

    // ---- Getters and Setters (Encapsulation) ----

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", adminRole='" + adminRole + '\'' +
                ", department='" + department + '\'' +
                ", permissions='" + permissions + '\'' +
                ", " + super.toString() +
                '}';
    }
}
