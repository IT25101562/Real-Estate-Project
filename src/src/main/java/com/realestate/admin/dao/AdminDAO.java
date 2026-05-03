package com.realestate.admin.dao;

import com.realestate.admin.model.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDAO — Data Access Object for Admin CRUD operations.
 * Interacts with the MS SQL Server database using JDBC.
 *
 * Operations: Create, Read (by ID, by username, all), Update, Delete,
 *             Authenticate, Search
 */
public class AdminDAO {

    private final DatabaseConnection dbConnection;

    /** Constructor — obtains the singleton database connection */
    public AdminDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    // ================================================================
    // CREATE — Register a new admin
    // ================================================================

    /**
     * Creates a new admin by inserting into both 'users' and 'admins' tables.
     * Uses a transaction to ensure both inserts succeed or both fail.
     *
     * @param admin The Admin object to create
     * @return true if creation was successful
     */
    public boolean createAdmin(Admin admin) {
        String insertUser = "INSERT INTO users (username, password, email, phone, full_name, user_type) "
                + "VALUES (?, ?, ?, ?, ?, 'ADMIN')";
        String insertAdmin = "INSERT INTO admins (user_id, admin_role, department, permissions) "
                + "VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert into users table
            PreparedStatement userStmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, admin.getUsername());
            userStmt.setString(2, admin.getPassword());
            userStmt.setString(3, admin.getEmail());
            userStmt.setString(4, admin.getPhone());
            userStmt.setString(5, admin.getFullName());
            userStmt.executeUpdate();

            // Get the generated user_id
            ResultSet generatedKeys = userStmt.getGeneratedKeys();
            int userId = 0;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }

            // Insert into admins table
            PreparedStatement adminStmt = conn.prepareStatement(insertAdmin);
            adminStmt.setInt(1, userId);
            adminStmt.setString(2, admin.getAdminRole());
            adminStmt.setString(3, admin.getDepartment());
            adminStmt.setString(4, admin.getPermissions());
            adminStmt.executeUpdate();

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            // Rollback on error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Error creating admin: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ================================================================
    // READ — Get admin by ID
    // ================================================================

    /**
     * Retrieves an admin by their user_id.
     * Joins the users and admins tables.
     *
     * @param userId The user ID to look up
     * @return Admin object, or null if not found
     */
    public Admin getAdminById(int userId) {
        String sql = "SELECT u.user_id, u.username, u.password, u.email, u.phone, u.full_name, "
                + "u.user_type, u.is_active, u.created_at, u.updated_at, "
                + "a.admin_id, a.admin_role, a.department, a.permissions, a.last_login "
                + "FROM users u INNER JOIN admins a ON u.user_id = a.user_id "
                + "WHERE u.user_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAdmin(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error getting admin by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ================================================================
    // READ — Get admin by username
    // ================================================================

    /**
     * Retrieves an admin by their username.
     *
     * @param username The username to look up
     * @return Admin object, or null if not found
     */
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT u.user_id, u.username, u.password, u.email, u.phone, u.full_name, "
                + "u.user_type, u.is_active, u.created_at, u.updated_at, "
                + "a.admin_id, a.admin_role, a.department, a.permissions, a.last_login "
                + "FROM users u INNER JOIN admins a ON u.user_id = a.user_id "
                + "WHERE u.username = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAdmin(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error getting admin by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ================================================================
    // READ — Get all admins
    // ================================================================

    /**
     * Retrieves all admins from the database.
     *
     * @return List of all Admin objects
     */
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.password, u.email, u.phone, u.full_name, "
                + "u.user_type, u.is_active, u.created_at, u.updated_at, "
                + "a.admin_id, a.admin_role, a.department, a.permissions, a.last_login "
                + "FROM users u INNER JOIN admins a ON u.user_id = a.user_id "
                + "ORDER BY u.created_at DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                admins.add(mapResultSetToAdmin(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error getting all admins: " + e.getMessage());
            e.printStackTrace();
        }
        return admins;
    }

    // ================================================================
    // UPDATE — Modify admin details
    // ================================================================

    /**
     * Updates an existing admin's details in both tables.
     *
     * @param admin The Admin object with updated values
     * @return true if update was successful
     */
    public boolean updateAdmin(Admin admin) {
        String updateUser = "UPDATE users SET username=?, password=?, email=?, phone=?, "
                + "full_name=?, is_active=? WHERE user_id=?";
        String updateAdmin = "UPDATE admins SET admin_role=?, department=?, permissions=? "
                + "WHERE user_id=?";

        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update users table
            PreparedStatement userStmt = conn.prepareStatement(updateUser);
            userStmt.setString(1, admin.getUsername());
            userStmt.setString(2, admin.getPassword());
            userStmt.setString(3, admin.getEmail());
            userStmt.setString(4, admin.getPhone());
            userStmt.setString(5, admin.getFullName());
            userStmt.setBoolean(6, admin.isActive());
            userStmt.setInt(7, admin.getUserId());
            userStmt.executeUpdate();

            // Update admins table
            PreparedStatement adminStmt = conn.prepareStatement(updateAdmin);
            adminStmt.setString(1, admin.getAdminRole());
            adminStmt.setString(2, admin.getDepartment());
            adminStmt.setString(3, admin.getPermissions());
            adminStmt.setInt(4, admin.getUserId());
            adminStmt.executeUpdate();

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Error updating admin: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ================================================================
    // DELETE — Remove admin account
    // ================================================================

    /**
     * Deletes an admin by their user_id.
     * CASCADE delete will also remove the admins record.
     *
     * @param userId The user ID to delete
     * @return true if deletion was successful
     */
    public boolean deleteAdmin(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting admin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ================================================================
    // AUTHENTICATE — Admin login validation
    // ================================================================

    /**
     * Authenticates an admin by username and password.
     * Uses the stored procedure sp_AuthenticateAdmin.
     *
     * @param username The admin's username
     * @param password The admin's password
     * @return Admin object if credentials are valid, null otherwise
     */
    public Admin authenticateAdmin(String username, String password) {
        String sql = "{CALL sp_AuthenticateAdmin(?, ?)}";
        
        // Check for placeholder
        if ("YOUR_ACTUAL_PASSWORD".equals(DatabaseConnection.getInstance().getPassword())) {
            System.err.println("⚠️ ERROR: You haven't updated the password in db.properties! Still using 'YOUR_ACTUAL_PASSWORD'.");
        }

        try (Connection conn = dbConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAdmin(rs);
            }

        } catch (SQLException e) {
            // Fallback: use direct query if stored procedure doesn't exist
            System.err.println("⚠️ Stored procedure failed, using direct query: " + e.getMessage());
            return authenticateAdminDirect(username, password);
        }
        return null;
    }

    /**
     * Fallback authentication using direct SQL query.
     */
    private Admin authenticateAdminDirect(String username, String password) {
        String sql = "SELECT u.user_id, u.username, u.password, u.email, u.phone, u.full_name, "
                + "u.user_type, u.is_active, u.created_at, u.updated_at, "
                + "a.admin_id, a.admin_role, a.department, a.permissions, a.last_login "
                + "FROM users u INNER JOIN admins a ON u.user_id = a.user_id "
                + "WHERE u.username = ? AND u.password = ? AND u.is_active = 1";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Update last login
                updateLastLogin(rs.getInt("user_id"));
                return mapResultSetToAdmin(rs);
            }

        } catch (SQLException e) {
            System.err.println("❌ ERROR: Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the last_login timestamp for an admin.
     */
    private void updateLastLogin(int userId) {
        String sql = "UPDATE admins SET last_login = GETDATE() WHERE user_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
        }
    }

    // ================================================================
    // SEARCH — Search admins by keyword
    // ================================================================

    /**
     * Searches admins by keyword (matches username, full_name, or email).
     *
     * @param keyword The search keyword
     * @return List of matching Admin objects
     */
    public List<Admin> searchAdmins(String keyword) {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.password, u.email, u.phone, u.full_name, "
                + "u.user_type, u.is_active, u.created_at, u.updated_at, "
                + "a.admin_id, a.admin_role, a.department, a.permissions, a.last_login "
                + "FROM users u INNER JOIN admins a ON u.user_id = a.user_id "
                + "WHERE u.username LIKE ? OR u.full_name LIKE ? OR u.email LIKE ? "
                + "ORDER BY u.full_name";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                admins.add(mapResultSetToAdmin(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error searching admins: " + e.getMessage());
            e.printStackTrace();
        }
        return admins;
    }

    // ================================================================
    // DASHBOARD STATS
    // ================================================================

    /**
     * Gets dashboard statistics (total users, admins, active/inactive).
     *
     * @return int array: [totalUsers, totalAdmins, activeUsers, inactiveUsers]
     */
    public int[] getDashboardStats() {
        int[] stats = new int[4]; // [totalUsers, totalAdmins, activeUsers, inactiveUsers]

        try (Connection conn = dbConnection.getConnection()) {
            // Total users
            PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet rs1 = stmt1.executeQuery();
            if (rs1.next()) stats[0] = rs1.getInt(1);

            // Total admins
            PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM admins");
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()) stats[1] = rs2.getInt(1);

            // Active users
            PreparedStatement stmt3 = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE is_active = 1");
            ResultSet rs3 = stmt3.executeQuery();
            if (rs3.next()) stats[2] = rs3.getInt(1);

            // Inactive users
            PreparedStatement stmt4 = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE is_active = 0");
            ResultSet rs4 = stmt4.executeQuery();
            if (rs4.next()) stats[3] = rs4.getInt(1);

        } catch (SQLException e) {
            System.err.println("Error getting dashboard stats: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }

    /**
     * Checks if a username already exists in the database.
     *
     * @param username The username to check
     * @return true if username exists
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if an email already exists in the database.
     *
     * @param email The email to check
     * @return true if email exists
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
        }
        return false;
    }

    // ================================================================
    // HELPER — Map ResultSet to Admin object
    // ================================================================

    /**
     * Maps a ResultSet row to an Admin object.
     * Reusable helper method used by all read operations.
     */
    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();

        // Map User fields
        admin.setUserId(rs.getInt("user_id"));
        admin.setUsername(rs.getString("username"));
        admin.setPassword(rs.getString("password"));
        admin.setEmail(rs.getString("email"));
        admin.setPhone(rs.getString("phone"));
        admin.setFullName(rs.getString("full_name"));
        admin.setUserType(rs.getString("user_type"));
        admin.setActive(rs.getBoolean("is_active"));

        // Map timestamps
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            admin.setCreatedAt(createdAt.toLocalDateTime());
        }
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            admin.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        // Map Admin-specific fields
        admin.setAdminId(rs.getInt("admin_id"));
        admin.setAdminRole(rs.getString("admin_role"));
        admin.setDepartment(rs.getString("department"));
        admin.setPermissions(rs.getString("permissions"));

        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            admin.setLastLogin(lastLogin.toLocalDateTime());
        }

        return admin;
    }
}
