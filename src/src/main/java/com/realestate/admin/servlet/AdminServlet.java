package com.realestate.admin.servlet;

import com.realestate.admin.dao.AdminDAO;
import com.realestate.admin.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * AdminServlet — Handles all admin CRUD operations.
 *
 * URL Patterns:
 *   GET  /admins                → List all admins (Control Panel)
 *   GET  /admins?action=view&id=1   → View admin profile
 *   GET  /admins?action=edit&id=1   → Show edit form
 *   GET  /admins?action=delete&id=1 → Delete admin
 *   GET  /admins?action=search&keyword=abc → Search admins
 *   POST /admins?action=update      → Process edit form
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admins"})
public class AdminServlet extends HttpServlet {

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

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "view":
                viewAdmin(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteAdmin(request, response);
                break;
            case "search":
                searchAdmins(request, response);
                break;
            case "list":
            default:
                listAdmins(request, response);
                break;
        }
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

        String action = request.getParameter("action");
        if ("update".equals(action)) {
            updateAdmin(request, response);
        }
    }

    // ---- List all admins (Admin Control Panel) ----
    private void listAdmins(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Admin> admins = adminDAO.getAllAdmins();
        request.setAttribute("admins", admins);
        request.setAttribute("pageTitle", "Admin Control Panel");
        request.getRequestDispatcher("/admin-list.jsp").forward(request, response);
    }

    // ---- View admin profile ----
    private void viewAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            Admin admin = adminDAO.getAdminById(userId);

            if (admin != null) {
                request.setAttribute("viewAdmin", admin);
                request.getRequestDispatcher("/admin-profile.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Admin not found.");
                listAdmins(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid admin ID.");
            listAdmins(request, response);
        }
    }

    // ---- Show edit form ----
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            Admin admin = adminDAO.getAdminById(userId);

            if (admin != null) {
                request.setAttribute("editAdmin", admin);
                request.getRequestDispatcher("/admin-edit.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Admin not found.");
                listAdmins(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid admin ID.");
            listAdmins(request, response);
        }
    }

    // ---- Process update form ----
    private void updateAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            Admin admin = adminDAO.getAdminById(userId);

            if (admin != null) {
                // Update fields from form data
                admin.setUsername(request.getParameter("username"));
                admin.setEmail(request.getParameter("email"));
                admin.setPhone(request.getParameter("phone"));
                admin.setFullName(request.getParameter("fullName"));
                admin.setAdminRole(request.getParameter("adminRole"));
                admin.setDepartment(request.getParameter("department"));

                // Handle password update (only if a new password is provided)
                String newPassword = request.getParameter("password");
                if (newPassword != null && !newPassword.trim().isEmpty()) {
                    admin.setPassword(newPassword.trim());
                }

                // Handle permissions (checkboxes)
                String[] perms = request.getParameterValues("permissions");
                if (perms != null) {
                    admin.setPermissions(String.join(",", perms));
                }

                // Handle active status
                String isActive = request.getParameter("isActive");
                admin.setActive("on".equals(isActive) || "true".equals(isActive));

                boolean success = adminDAO.updateAdmin(admin);

                if (success) {
                    // Update session if editing own profile
                    HttpSession session = request.getSession();
                    Admin currentAdmin = (Admin) session.getAttribute("admin");
                    if (currentAdmin.getUserId() == userId) {
                        Admin refreshed = adminDAO.getAdminById(userId);
                        session.setAttribute("admin", refreshed);
                        session.setAttribute("adminName", refreshed.getFullName());
                        session.setAttribute("adminRole", refreshed.getAdminRole());
                    }

                    request.setAttribute("successMessage", "Admin updated successfully!");
                } else {
                    request.setAttribute("errorMessage", "Failed to update admin.");
                }
            } else {
                request.setAttribute("errorMessage", "Admin not found.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid admin ID.");
        }

        listAdmins(request, response);
    }

    // ---- Delete admin ----
    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            // Prevent deleting yourself
            HttpSession session = request.getSession();
            Admin currentAdmin = (Admin) session.getAttribute("admin");
            if (currentAdmin.getUserId() == userId) {
                request.setAttribute("errorMessage", "You cannot delete your own account!");
                listAdmins(request, response);
                return;
            }

            boolean success = adminDAO.deleteAdmin(userId);
            if (success) {
                request.setAttribute("successMessage", "Admin deleted successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to delete admin.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid admin ID.");
        }

        listAdmins(request, response);
    }

    // ---- Search admins ----
    private void searchAdmins(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        List<Admin> admins;

        if (keyword != null && !keyword.trim().isEmpty()) {
            admins = adminDAO.searchAdmins(keyword.trim());
            request.setAttribute("searchKeyword", keyword);
        } else {
            admins = adminDAO.getAllAdmins();
        }

        request.setAttribute("admins", admins);
        request.setAttribute("pageTitle", "Search Results");
        request.getRequestDispatcher("/admin-list.jsp").forward(request, response);
    }
}
