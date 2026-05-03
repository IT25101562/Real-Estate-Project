package com.realestate.admin.servlet;

import com.realestate.admin.dao.AdminDAO;
import com.realestate.admin.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * DashboardServlet — Displays the admin dashboard with system statistics.
 * GET /dashboard → Show dashboard with stats
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

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

        Admin currentAdmin = (Admin) session.getAttribute("admin");

        // Get dashboard statistics
        int[] stats = adminDAO.getDashboardStats();
        request.setAttribute("totalUsers", stats[0]);
        request.setAttribute("totalAdmins", stats[1]);
        request.setAttribute("activeUsers", stats[2]);
        request.setAttribute("inactiveUsers", stats[3]);

        // Get recent admins for the dashboard list
        List<Admin> recentAdmins = adminDAO.getAllAdmins();
        request.setAttribute("recentAdmins", recentAdmins);
        request.setAttribute("currentAdmin", currentAdmin);

        request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
    }
}
