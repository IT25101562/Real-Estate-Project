package com.realestate.admin.servlet;

import com.realestate.admin.dao.AdminDAO;
import com.realestate.admin.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminRegistrationServlet — Handles new admin registration.
 * GET  /register → Display registration form
 * POST /register → Process registration
 */
@WebServlet(name = "AdminRegistrationServlet", urlPatterns = {"/register"})
public class AdminRegistrationServlet extends HttpServlet {

    private AdminDAO adminDAO;

    @Override
    public void init() throws ServletException {
        adminDAO = new AdminDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if admin is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Only SUPER_ADMIN can register new admins
        Admin currentAdmin = (Admin) session.getAttribute("admin");
        if (!currentAdmin.canManageAdmins()) {
            request.setAttribute("errorMessage", "You do not have permission to register new admins.");
            request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/admin-register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if admin is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Only SUPER_ADMIN can register new admins
        Admin currentAdmin = (Admin) session.getAttribute("admin");
        if (!currentAdmin.canManageAdmins()) {
            request.setAttribute("errorMessage", "You do not have permission to register new admins.");
            request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
            return;
        }

        // Get form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String fullName = request.getParameter("fullName");
        String adminRole = request.getParameter("adminRole");
        String department = request.getParameter("department");

        // Handle permissions checkboxes
        String[] perms = request.getParameterValues("permissions");
        String permissions = (perms != null) ? String.join(",", perms) : "VIEW_USERS,VIEW_PROPERTIES";

        // ---- Validation ----
        StringBuilder errors = new StringBuilder();

        if (username == null || username.trim().isEmpty()) {
            errors.append("Username is required. ");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.append("Password is required. ");
        }
        if (password != null && !password.equals(confirmPassword)) {
            errors.append("Passwords do not match. ");
        }
        if (email == null || email.trim().isEmpty()) {
            errors.append("Email is required. ");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            errors.append("Full name is required. ");
        }

        // Check for duplicate username/email
        if (username != null && adminDAO.usernameExists(username.trim())) {
            errors.append("Username already exists. ");
        }
        if (email != null && adminDAO.emailExists(email.trim())) {
            errors.append("Email already exists. ");
        }

        if (errors.length() > 0) {
            request.setAttribute("errorMessage", errors.toString().trim());
            // Preserve form data
            request.setAttribute("formUsername", username);
            request.setAttribute("formEmail", email);
            request.setAttribute("formPhone", phone);
            request.setAttribute("formFullName", fullName);
            request.setAttribute("formAdminRole", adminRole);
            request.setAttribute("formDepartment", department);
            request.getRequestDispatcher("/admin-register.jsp").forward(request, response);
            return;
        }

        // ---- Create Admin ----
        Admin newAdmin = new Admin();
        newAdmin.setUsername(username.trim());
        newAdmin.setPassword(password.trim());
        newAdmin.setEmail(email.trim());
        newAdmin.setPhone(phone != null ? phone.trim() : "");
        newAdmin.setFullName(fullName.trim());
        newAdmin.setAdminRole(adminRole != null ? adminRole : "MODERATOR");
        newAdmin.setDepartment(department != null ? department.trim() : "");
        newAdmin.setPermissions(permissions);

        boolean success = adminDAO.createAdmin(newAdmin);

        if (success) {
            request.setAttribute("successMessage",
                    "Admin '" + username + "' registered successfully!");
            request.getRequestDispatcher("/admin-register.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Failed to register admin. Please try again.");
            request.getRequestDispatcher("/admin-register.jsp").forward(request, response);
        }
    }
}
