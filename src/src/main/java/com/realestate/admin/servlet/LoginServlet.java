package com.realestate.admin.servlet;

import com.realestate.admin.dao.AdminDAO;
import com.realestate.admin.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * LoginServlet — Handles admin login and logout.
 * GET  /login → Display the login page
 * POST /login → Authenticate credentials
 * GET  /login?action=logout → Logout and invalidate session
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private AdminDAO adminDAO;

    @Override
    public void init() throws ServletException {
        adminDAO = new AdminDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Handle logout
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login?message=logged_out");
            return;
        }

        // Check if already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("admin") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Show login page
        String message = request.getParameter("message");
        if ("logged_out".equals(message)) {
            request.setAttribute("successMessage", "You have been logged out successfully.");
        }

        request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter both username and password.");
            request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
            return;
        }

        // Authenticate
        Admin admin = adminDAO.authenticateAdmin(username.trim(), password.trim());

        if (admin != null) {
            // Login successful — create session
            HttpSession session = request.getSession();
            session.setAttribute("admin", admin);
            session.setAttribute("adminId", admin.getUserId());
            session.setAttribute("adminName", admin.getFullName());
            session.setAttribute("adminRole", admin.getAdminRole());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            // Login failed
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
        }
    }
}
