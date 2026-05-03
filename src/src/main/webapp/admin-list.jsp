<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Control Panel - Real Estate Portal</title>
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
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-link" id="navDashboard">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>
                    Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/admins" class="nav-link active" id="navAdmins">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
                    Admin Control Panel
                </a>
                <a href="${pageContext.request.contextPath}/register" class="nav-link" id="navRegister">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="8.5" cy="7" r="4"/><line x1="20" y1="8" x2="20" y2="14"/><line x1="23" y1="11" x2="17" y2="11"/></svg>
                    Register Admin
                </a>
                <a href="${pageContext.request.contextPath}/admins?action=view&id=${sessionScope.admin.userId}" class="nav-link" id="navProfile">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                    My Profile
                </a>
            </nav>
            <div class="sidebar-footer">
                <div class="admin-info">
                    <div class="admin-avatar">${sessionScope.adminName.substring(0,1)}</div>
                    <div class="admin-details">
                        <span class="admin-name">${sessionScope.adminName}</span>
                        <span class="admin-role-tag">${sessionScope.adminRole}</span>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/login?action=logout" class="btn-logout" id="logoutBtn">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                    Logout
                </a>
            </div>
        </aside>

        <!-- Main Content -->
        <main class="main-content">
            <header class="topbar">
                <button class="menu-toggle" id="menuToggle" onclick="toggleSidebar()">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="18" x2="21" y2="18"/></svg>
                </button>
                <h2 class="page-title">${not empty pageTitle ? pageTitle : 'Admin Control Panel'}</h2>
            </header>

            <div class="content-wrapper">
                <!-- Messages -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>
                        ${errorMessage}
                    </div>
                </c:if>
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
                        ${successMessage}
                    </div>
                </c:if>

                <!-- Search and Actions Bar -->
                <div class="section-card">
                    <div class="toolbar">
                        <form action="${pageContext.request.contextPath}/admins" method="get" class="search-form" id="searchForm">
                            <input type="hidden" name="action" value="search">
                            <div class="search-input-wrapper">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
                                <input type="text" name="keyword" placeholder="Search by name, username, or email..."
                                       value="${searchKeyword}" id="searchInput">
                            </div>
                            <button type="submit" class="btn btn-primary btn-sm" id="searchBtn">Search</button>
                        </form>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm" id="addNewBtn">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                            Add New
                        </a>
                    </div>
                </div>

                <!-- Admins Table -->
                <div class="section-card">
                    <div class="section-header">
                        <h3>All Administrators (${admins.size()})</h3>
                        <c:if test="${not empty searchKeyword}">
                            <a href="${pageContext.request.contextPath}/admins" class="btn btn-sm btn-secondary">Clear Search</a>
                        </c:if>
                    </div>
                    <div class="table-responsive">
                        <table class="data-table" id="adminsTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Department</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="admin" items="${admins}">
                                    <tr>
                                        <td><strong>#${admin.userId}</strong></td>
                                        <td>
                                            <div class="user-cell">
                                                <div class="user-avatar-sm">${admin.fullName.substring(0,1)}</div>
                                                ${admin.fullName}
                                            </div>
                                        </td>
                                        <td><code>${admin.username}</code></td>
                                        <td>${admin.email}</td>
                                        <td>
                                            <span class="badge ${admin.adminRole == 'SUPER_ADMIN' ? 'badge-primary' : 'badge-secondary'}">
                                                ${admin.adminRole}
                                            </span>
                                        </td>
                                        <td>${admin.department}</td>
                                        <td>
                                            <span class="badge ${admin.active ? 'badge-success' : 'badge-danger'}">
                                                ${admin.active ? 'Active' : 'Inactive'}
                                            </span>
                                        </td>
                                        <td>
                                            <div class="action-buttons">
                                                <a href="${pageContext.request.contextPath}/admins?action=view&id=${admin.userId}"
                                                   class="btn-icon btn-icon-view" title="View Profile">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admins?action=edit&id=${admin.userId}"
                                                   class="btn-icon btn-icon-edit" title="Edit">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admins?action=delete&id=${admin.userId}"
                                                   class="btn-icon btn-icon-delete" title="Delete"
                                                   onclick="return confirm('Are you sure you want to delete admin: ${admin.fullName}?');">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/><line x1="10" y1="11" x2="10" y2="17"/><line x1="14" y1="11" x2="14" y2="17"/></svg>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty admins}">
                                    <tr><td colspan="8" class="text-center">No administrators found.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
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
