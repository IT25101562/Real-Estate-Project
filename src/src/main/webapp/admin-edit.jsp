<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Admin - Real Estate Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <div class="app-layout">
        <!-- Sidebar -->
        <aside class="sidebar" id="sidebar">
            <div class="sidebar-header">
                <div class="sidebar-logo">
                    <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
                    <span>RealEstate</span>
                </div>
            </div>
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-link"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg> Dashboard</a>
                <a href="${pageContext.request.contextPath}/admins" class="nav-link active"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg> Admin Control Panel</a>
                <a href="${pageContext.request.contextPath}/register" class="nav-link"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="8.5" cy="7" r="4"/><line x1="20" y1="8" x2="20" y2="14"/><line x1="23" y1="11" x2="17" y2="11"/></svg> Register Admin</a>
                <a href="${pageContext.request.contextPath}/admins?action=view&id=${sessionScope.admin.userId}" class="nav-link"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg> My Profile</a>
            </nav>
            <div class="sidebar-footer">
                <div class="admin-info">
                    <div class="admin-avatar">${sessionScope.adminName.substring(0,1)}</div>
                    <div class="admin-details">
                        <span class="admin-name">${sessionScope.adminName}</span>
                        <span class="admin-role-tag">${sessionScope.adminRole}</span>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/login?action=logout" class="btn-logout">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                    Logout
                </a>
            </div>
        </aside>

        <!-- Main Content -->
        <main class="main-content">
            <header class="topbar">
                <button class="menu-toggle" onclick="toggleSidebar()">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="18" x2="21" y2="18"/></svg>
                </button>
                <h2 class="page-title">Edit Admin: ${editAdmin.fullName}</h2>
            </header>

            <div class="content-wrapper">
                <div class="section-card">
                    <div class="section-header">
                        <h3>Edit Admin Details</h3>
                        <a href="${pageContext.request.contextPath}/admins" class="btn btn-sm btn-secondary">Back to List</a>
                    </div>

                    <form action="${pageContext.request.contextPath}/admins" method="post" class="form-grid" id="editForm">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="userId" value="${editAdmin.userId}">

                        <!-- Personal Information -->
                        <div class="form-section-title">Personal Information</div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="fullName">Full Name *</label>
                                <input type="text" id="fullName" name="fullName" value="${editAdmin.fullName}" required>
                            </div>
                            <div class="form-group">
                                <label for="email">Email *</label>
                                <input type="email" id="email" name="email" value="${editAdmin.email}" required>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="phone">Phone</label>
                                <input type="tel" id="phone" name="phone" value="${editAdmin.phone}">
                            </div>
                            <div class="form-group">
                                <label for="department">Department</label>
                                <input type="text" id="department" name="department" value="${editAdmin.department}">
                            </div>
                        </div>

                        <!-- Account -->
                        <div class="form-section-title">Account Settings</div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="username">Username *</label>
                                <input type="text" id="username" name="username" value="${editAdmin.username}" required>
                            </div>
                            <div class="form-group">
                                <label for="adminRole">Admin Role *</label>
                                <select id="adminRole" name="adminRole" required>
                                    <option value="MODERATOR" ${editAdmin.adminRole == 'MODERATOR' ? 'selected' : ''}>Moderator</option>
                                    <option value="SUPER_ADMIN" ${editAdmin.adminRole == 'SUPER_ADMIN' ? 'selected' : ''}>Super Admin</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="password">New Password <small>(leave blank to keep current)</small></label>
                                <input type="password" id="password" name="password" placeholder="Enter new password">
                            </div>
                            <div class="form-group">
                                <label for="isActive">Account Status</label>
                                <div class="toggle-wrapper">
                                    <label class="toggle-switch">
                                        <input type="checkbox" id="isActive" name="isActive" ${editAdmin.active ? 'checked' : ''}>
                                        <span class="toggle-slider"></span>
                                    </label>
                                    <span class="toggle-label">${editAdmin.active ? 'Active' : 'Inactive'}</span>
                                </div>
                            </div>
                        </div>

                        <!-- Permissions -->
                        <div class="form-section-title">Permissions</div>

                        <div class="permissions-grid">
                            <label class="checkbox-label">
                                <input type="checkbox" name="permissions" value="VIEW_USERS"
                                       ${editAdmin.permissions.contains('VIEW_USERS') ? 'checked' : ''}>
                                <span class="checkbox-custom"></span>
                                View Users
                            </label>
                            <label class="checkbox-label">
                                <input type="checkbox" name="permissions" value="MANAGE_USERS"
                                       ${editAdmin.permissions.contains('MANAGE_USERS') ? 'checked' : ''}>
                                <span class="checkbox-custom"></span>
                                Manage Users
                            </label>
                            <label class="checkbox-label">
                                <input type="checkbox" name="permissions" value="VIEW_PROPERTIES"
                                       ${editAdmin.permissions.contains('VIEW_PROPERTIES') ? 'checked' : ''}>
                                <span class="checkbox-custom"></span>
                                View Properties
                            </label>
                            <label class="checkbox-label">
                                <input type="checkbox" name="permissions" value="MANAGE_PROPERTIES"
                                       ${editAdmin.permissions.contains('MANAGE_PROPERTIES') ? 'checked' : ''}>
                                <span class="checkbox-custom"></span>
                                Manage Properties
                            </label>
                            <label class="checkbox-label">
                                <input type="checkbox" name="permissions" value="VIEW_REPORTS"
                                       ${editAdmin.permissions.contains('VIEW_REPORTS') ? 'checked' : ''}>
                                <span class="checkbox-custom"></span>
                                View Reports
                            </label>
                            <label class="checkbox-label">
                                <input type="checkbox" name="permissions" value="MANAGE_ADMINS"
                                       ${editAdmin.permissions.contains('MANAGE_ADMINS') ? 'checked' : ''}>
                                <span class="checkbox-custom"></span>
                                Manage Admins
                            </label>
                        </div>

                        <!-- Submit -->
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary" id="updateBtn">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/><polyline points="17 21 17 13 7 13 7 21"/><polyline points="7 3 7 8 15 8"/></svg>
                                Save Changes
                            </button>
                            <a href="${pageContext.request.contextPath}/admins" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>

    <script>
        function toggleSidebar() {
            document.getElementById('sidebar').classList.toggle('sidebar-collapsed');
            document.querySelector('.main-content').classList.toggle('main-expanded');
        }

        // Update permissions based on admin role
        document.getElementById('adminRole').addEventListener('change', function() {
            var role = this.value;
            var checkboxes = document.querySelectorAll('input[name="permissions"]');
            
            if (role === 'SUPER_ADMIN') {
                checkboxes.forEach(function(cb) {
                    cb.checked = true;
                });
            } else if (role === 'MODERATOR') {
                var modPerms = ['VIEW_USERS', 'VIEW_PROPERTIES', 'VIEW_REPORTS'];
                checkboxes.forEach(function(cb) {
                    cb.checked = modPerms.includes(cb.value);
                });
            }
        });

        // Toggle label update
        document.getElementById('isActive').addEventListener('change', function() {
            document.querySelector('.toggle-label').textContent = this.checked ? 'Active' : 'Inactive';
        });
    </script>
</body>
</html>
