-- =====================================================
-- Real Estate Property Listings Portal
-- Component 05: Admin Management - Database Setup
-- Microsoft SQL Server
-- =====================================================

-- Create Database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'RealEstateDB')
BEGIN
    CREATE DATABASE RealEstateDB;
END
GO

USE RealEstateDB;
GO

-- =====================================================
-- Table: users (Base table for all user types)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'users')
BEGIN
    CREATE TABLE users (
        user_id         INT IDENTITY(1,1) PRIMARY KEY,
        username        NVARCHAR(50) NOT NULL UNIQUE,
        password        NVARCHAR(255) NOT NULL,
        email           NVARCHAR(100) NOT NULL UNIQUE,
        phone           NVARCHAR(20),
        full_name       NVARCHAR(100) NOT NULL,
        user_type       NVARCHAR(20) NOT NULL DEFAULT 'USER',  -- 'ADMIN' or 'USER'
        is_active       BIT NOT NULL DEFAULT 1,
        created_at      DATETIME NOT NULL DEFAULT GETDATE(),
        updated_at      DATETIME NOT NULL DEFAULT GETDATE()
    );
END
GO

-- =====================================================
-- Table: admins (Extends users with admin-specific data)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'admins')
BEGIN
    CREATE TABLE admins (
        admin_id        INT IDENTITY(1,1) PRIMARY KEY,
        user_id         INT NOT NULL UNIQUE,
        admin_role      NVARCHAR(30) NOT NULL DEFAULT 'MODERATOR',  -- 'SUPER_ADMIN', 'MODERATOR'
        department      NVARCHAR(50),
        permissions     NVARCHAR(500) NOT NULL DEFAULT 'VIEW_USERS,VIEW_PROPERTIES',
        last_login      DATETIME,
        CONSTRAINT FK_admins_users FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
    );
END
GO

-- =====================================================
-- Trigger: Update updated_at on users table
-- =====================================================
IF EXISTS (SELECT * FROM sys.triggers WHERE name = 'trg_users_update')
    DROP TRIGGER trg_users_update;
GO

CREATE TRIGGER trg_users_update
ON users
AFTER UPDATE
AS
BEGIN
    UPDATE users
    SET updated_at = GETDATE()
    FROM users u
    INNER JOIN inserted i ON u.user_id = i.user_id;
END
GO

-- =====================================================
-- Stored Procedure: Authenticate Admin
-- =====================================================
IF EXISTS (SELECT * FROM sys.procedures WHERE name = 'sp_AuthenticateAdmin')
    DROP PROCEDURE sp_AuthenticateAdmin;
GO

CREATE PROCEDURE sp_AuthenticateAdmin
    @username NVARCHAR(50),
    @password NVARCHAR(255)
AS
BEGIN
    SELECT u.user_id, u.username, u.password, u.email, u.phone, u.full_name,
           u.user_type, u.is_active, u.created_at, u.updated_at,
           a.admin_id, a.admin_role, a.department, a.permissions, a.last_login
    FROM users u
    INNER JOIN admins a ON u.user_id = a.user_id
    WHERE u.username = @username AND u.password = @password AND u.is_active = 1;

    -- Update last login time
    IF @@ROWCOUNT > 0
    BEGIN
        UPDATE admins
        SET last_login = GETDATE()
        FROM admins a
        INNER JOIN users u ON a.user_id = u.user_id
        WHERE u.username = @username;
    END
END
GO

-- =====================================================
-- Stored Procedure: Get Admin Dashboard Stats
-- =====================================================
IF EXISTS (SELECT * FROM sys.procedures WHERE name = 'sp_GetDashboardStats')
    DROP PROCEDURE sp_GetDashboardStats;
GO

CREATE PROCEDURE sp_GetDashboardStats
AS
BEGIN
    SELECT
        (SELECT COUNT(*) FROM users) AS total_users,
        (SELECT COUNT(*) FROM admins) AS total_admins,
        (SELECT COUNT(*) FROM users WHERE is_active = 1) AS active_users,
        (SELECT COUNT(*) FROM users WHERE is_active = 0) AS inactive_users;
END
GO

-- =====================================================
-- Insert Sample Data
-- =====================================================

-- Insert a Super Admin
IF NOT EXISTS (SELECT * FROM users WHERE username = 'superadmin')
BEGIN
    INSERT INTO users (username, password, email, phone, full_name, user_type)
    VALUES ('superadmin', 'admin123', 'superadmin@realestate.com', '+94771234567', 'Super Administrator', 'ADMIN');

    DECLARE @superAdminId INT = SCOPE_IDENTITY();

    INSERT INTO admins (user_id, admin_role, department, permissions)
    VALUES (@superAdminId, 'SUPER_ADMIN', 'Management', 'VIEW_USERS,MANAGE_USERS,VIEW_PROPERTIES,MANAGE_PROPERTIES,VIEW_REPORTS,MANAGE_ADMINS');
END
GO

-- Insert a Moderator Admin
IF NOT EXISTS (SELECT * FROM users WHERE username = 'moderator1')
BEGIN
    INSERT INTO users (username, password, email, phone, full_name, user_type)
    VALUES ('moderator1', 'mod123', 'moderator@realestate.com', '+94779876543', 'John Moderator', 'ADMIN');

    DECLARE @modId INT = SCOPE_IDENTITY();

    INSERT INTO admins (user_id, admin_role, department, permissions)
    VALUES (@modId, 'MODERATOR', 'Support', 'VIEW_USERS,VIEW_PROPERTIES,VIEW_REPORTS');
END
GO

-- Insert sample regular users
IF NOT EXISTS (SELECT * FROM users WHERE username = 'john_doe')
BEGIN
    INSERT INTO users (username, password, email, phone, full_name, user_type)
    VALUES ('john_doe', 'user123', 'john@example.com', '+94771111111', 'John Doe', 'USER');
END
GO

IF NOT EXISTS (SELECT * FROM users WHERE username = 'jane_smith')
BEGIN
    INSERT INTO users (username, password, email, phone, full_name, user_type)
    VALUES ('jane_smith', 'user123', 'jane@example.com', '+94772222222', 'Jane Smith', 'USER');
END
GO

PRINT 'Database setup completed successfully!';
GO
