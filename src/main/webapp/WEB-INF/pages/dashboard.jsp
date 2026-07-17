<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tasks.css">
</head>
<body>

<div class="app-layout">


    <aside class="sidebar">
        <div class="sidebar-brand">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">
                <img src="${pageContext.request.contextPath}/tegar.png"  alt="dashboard" style="width: 200px; height: auto;">

            </a>

        </div>

        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="active">Dashboard</a>
            <a href="${pageContext.request.contextPath}/task">My Tasks</a>
        </nav>

        <a href="${pageContext.request.contextPath}/logout" class="logout-link">Log out</a>
    </aside>


    <main class="main-content">
        <h1>Dashboard</h1>
        <p class="welcome-text">Welcome back, ${sessionScope.username}</p>

        <div class="stats">
            <div class="stat-card">
                <p class="stat-number">${totalCount}</p>
                <p class="stat-label">Total</p>
            </div>

            <div class="stat-card">
                <p class="stat-number">${completedCount}</p>
                <p class="stat-label">Completed</p>
            </div>

            <div class="stat-card">
                <p class="stat-number">${pendingCount}</p>
                <p class="stat-label">Pending</p>
            </div>

            <div class="stat-card">
                <p class="stat-number">${overdueCount}</p>
                <p class="stat-label">Overdue</p>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/task" class="go-to-tasks-btn">Go to My Tasks</a>
    </main>

</div>

</body>
</html>
