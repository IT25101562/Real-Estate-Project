package com.realestate.admin;

import com.realestate.admin.dao.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Diagnostic tool to check MS SQL Server connection and database setup.
 */
public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("--- MS SQL Server Connection Diagnostic ---");

        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            System.out.println("1. Attempting to get connection...");

            try (Connection conn = db.getConnection()) {
                System.out.println("CONNECTION SUCCESSFUL!");

                System.out.println("2. Checking database tables...");
                Statement stmt = conn.createStatement();

                // Check users table
                try {
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
                    if (rs.next()) {
                        System.out.println("'users' table exists. Count: " + rs.getInt(1));
                    }
                } catch (Exception e) {
                    System.out.println("'users' table MISSING: " + e.getMessage());
                }

                // Check admins table
                try {
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM admins");
                    if (rs.next()) {
                        System.out.println("'admins' table exists. Count: " + rs.getInt(1));
                    }
                } catch (Exception e) {
                    System.out.println("'admins' table MISSING: " + e.getMessage());
                }

                // Check sample user
                try {
                    ResultSet rs = stmt.executeQuery("SELECT full_name FROM users WHERE username = 'superadmin'");
                    if (rs.next()) {
                        System.out.println("'superadmin' user exists: " + rs.getString(1));
                    } else {
                        System.out.println("'superadmin' user NOT FOUND. Run setup.sql!");
                    }
                } catch (Exception e) {
                    System.out.println("Could not query superadmin: " + e.getMessage());
                }

            }
        } catch (Exception e) {
            System.out.println("\nCONNECTION FAILED!");
            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
