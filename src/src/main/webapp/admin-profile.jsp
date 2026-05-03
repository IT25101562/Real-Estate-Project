<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Profile - Real Estate Portal</title>
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
                <a href="${pageContext.request.contextPath}/admins" class="nav-link"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg> Admin Control Panel</a>
                <a href="${pageContext.request.contextPath}/register" class="nav-link"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="8.5" cy="7" r="4"/><line x1="20" y1="8" x2="20" y2="14"/><line x1="23" y1="11" x2="17" y2="11"/></svg> Register Admin</a>
                <a href="${pageContext.request.contextPath}/admins?action=view&id=${sessionScope.admin.userId}" class="nav-link active"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg> My Profile</a>
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
                <h2 class="page-title">Admin Profile</h2>
            </header>

            <div class="content-wrapper">
                <!-- Profile Header Card -->
                <div class="profile-card">
                    <div class="profile-header-section">
                        <div class="profile-avatar-large">${viewAdmin.fullName.substring(0,1)}</div>
                        <div class="profile-header-info">
                            <h2>${viewAdmin.fullName}</h2>
                            <p class="profile-username">@${viewAdmin.username}</p>
                            <div class="profile-badges">
                                <span class="badge ${viewAdmin.adminRole == 'SUPER_ADMIN' ? 'badge-primary' : 'badge-secondary'}">
                                    ${viewAdmin.adminRole}
                                </span>
                                <span class="badge ${viewAdmin.active ? 'badge-success' : 'badge-danger'}">
                                    ${viewAdmin.active ? 'Active' : 'Inactive'}
                                </span>
                            </div>
                        </div>
                        <div class="profile-actions">
                            <a href="${pageContext.request.contextPath}/admins?action=edit&id=${viewAdmin.userId}" class="btn btn-primary btn-sm" id="editProfileBtn">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                                Edit Profile
                            </a>
                            <a href="${pageContext.request.contextPath}/admins" class="btn btn-secondary btn-sm">Back to List</a>
                        </div>
                    </div>
                </div>

                <!-- Profile Details -->
                <div class="profile-details-grid">
                    <!-- Contact Information -->
                    <div class="section-card">
                        <div class="section-header">
                            <h3>
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                                Contact Information
                            </h3>
                        </div>
                        <div class="detail-list">
                            <div class="detail-item">
                                <span class="detail-label">Email</span>
                                <span class="detail-value">${viewAdmin.email}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Phone</span>
                                <span class="detail-value">${not empty viewAdmin.phone ? viewAdmin.phone : 'Not provided'}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Department</span>
                                <span class="detail-value">${not empty viewAdmin.department ? viewAdmin.department : 'Not assigned'}</span>
                            </div>
                        </div>
                    </div>

                    <!-- Account Information -->
                    <div class="section-card">
                        <div class="section-header">
                            <h3>
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
                                Account Information
                            </h3>
                        </div>
                        <div class="detail-list">
                            <div class="detail-item">
                                <span class="detail-label">User ID</span>
                                <span class="detail-value">#${viewAdmin.userId}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Admin ID</span>
                                <span class="detail-value">#${viewAdmin.adminId}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Created</span>
                                <span class="detail-value">${viewAdmin.createdAt}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Last Login</span>
                                <span class="detail-value">${not empty viewAdmin.lastLogin ? viewAdmin.lastLogin : 'Never'}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Permissions -->
                <div class="section-card">
                    <div class="section-header">
                        <h3>
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                            Permissions
                        </h3>
                    </div>
                    <div class="permissions-display">
                        <c:forEach var="perm" items="${viewAdmin.permissionList}">
                            <span class="permission-tag">
                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>
                                ${perm}
                            </span>
                        </c:forEach>
                    </div>
                </div>


            </div>
        </main>
    </div>

    <script>
        function toggleSidebar() {
            document.getElementById('sidebar').classList.toggle('sidebar-collapsed');
            document.querySelector('.main-content').classList.toggle('main-expanded');
        }
    </script>
</body>
</html>
