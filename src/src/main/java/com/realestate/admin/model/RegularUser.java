package com.realestate.admin.model;

/**
 * RegularUser class — extends User.
 * Demonstrates: INHERITANCE  (RegularUser extends User)
 *               POLYMORPHISM (overrides getRole(), hasPermission(), getDisplayInfo()
 *                             differently from Admin)
 *
 * This class exists to show polymorphism: same method names,
 * different behavior compared to Admin.
 */
public class RegularUser extends User {

    // ---- Constructors ----

    /** Default constructor */
    public RegularUser() {
        super();
        setUserType("USER");
    }

    /** Parameterized constructor */
    public RegularUser(int userId, String username, String password,
                       String email, String phone, String fullName) {
        super(userId, username, password, email, phone, fullName, "USER");
    }

    // ============================================================
    // POLYMORPHISM — Different implementations from Admin
    // ============================================================

    /**
     * Regular users always have the role "USER".
     * Compare with Admin.getRole() which returns "SUPER_ADMIN" or "MODERATOR".
     */
    @Override
    public String getRole() {
        return "USER";
    }

    /**
     * Regular users have NO admin permissions.
     * Compare with Admin.hasPermission() which checks the permission list.
     */
    @Override
    public boolean hasPermission(String permission) {
        return false; // Regular users never have admin permissions
    }

    /**
     * Regular user display format is different from Admin's.
     * Shows a simpler format without role/department info.
     */
    @Override
    public String getDisplayInfo() {
        return String.format("[USER] %s | Email: %s | Phone: %s",
                getFullName(), getEmail(), getPhone());
    }

    @Override
    public String toString() {
        return "RegularUser{" + super.toString() + '}';
    }
}
