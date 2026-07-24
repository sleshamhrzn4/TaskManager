<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
  <title>TEGER | All Tasks</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tasks.css">
</head>
<body>

<div class="app-layout">

  <aside class="sidebar">
    <div class="sidebar-brand"><span class="check">&#10003;</span> TEGER</div>
    <nav class="sidebar-nav">
      <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
      <a href="${pageContext.request.contextPath}/task">Board</a>
      <a href="${pageContext.request.contextPath}/tasklist" class="active">All Tasks</a>
    </nav>
    <a href="${pageContext.request.contextPath}/logout" class="logout-link">Log out</a>
  </aside>

  <main class="main-content">
    <h1>All Tasks</h1>

    <!-- Search + Filter + Sort -->
    <form action="${pageContext.request.contextPath}/tasks" method="get" class="list-controls">
      <input type="text" name="search" placeholder="Search by title..." value="${search}"/>

      <select name="priority">
        <option value="all" ${priorityFilter == 'all' ? 'selected' : ''}>All Priorities</option>
        <option value="Low" ${priorityFilter == 'Low' ? 'selected' : ''}>Low</option>
        <option value="Medium" ${priorityFilter == 'Medium' ? 'selected' : ''}>Medium</option>
        <option value="High" ${priorityFilter == 'High' ? 'selected' : ''}>High</option>
      </select>

      <select name="status">
        <option value="all" ${statusFilter == 'all' ? 'selected' : ''}>All Statuses</option>
        <option value="todo" ${statusFilter == 'todo' ? 'selected' : ''}>To Do</option>
        <option value="inprogress" ${statusFilter == 'inprogress' ? 'selected' : ''}>In Progress</option>
        <option value="done" ${statusFilter == 'done' ? 'selected' : ''}>Done</option>
      </select>

      <select name="sortBy">
        <option value="dueDate" ${sortBy == 'dueDate' ? 'selected' : ''}>Due Date</option>
        <option value="priority" ${sortBy == 'priority' ? 'selected' : ''}>Priority</option>
        <option value="createdDate" ${sortBy == 'createdDate' ? 'selected' : ''}>Created Date</option>
        <option value="title" ${sortBy == 'title' ? 'selected' : ''}>Title</option>
      </select>

      <select name="sortDir">
        <option value="ASC" ${sortDir == 'ASC' ? 'selected' : ''}>Ascending</option>
        <option value="DESC" ${sortDir == 'DESC' ? 'selected' : ''}>Descending</option>
      </select>

      <button type="submit">Apply</button>
    </form>

    <c:if test="${not empty errorMessage}">
      <div class="error-msg">${errorMessage}</div>
    </c:if>

    <!-- Task list -->
    <table class="task-table">
      <thead>
      <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Priority</th>
        <th>Status</th>
        <th>Due Date</th>
        <th>Created Date</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="task" items="${tasks}">
        <tr>
          <td>${task.title}</td>
          <td>${task.description}</td>
          <td><span class="dot dot-${fn:toLowerCase(task.priority)}"></span>${task.priority}</td>
          <td>${task.status}</td>
          <td>${task.dueDate}</td>
          <td>${task.createdDate}</td>
        </tr>
      </c:forEach>

      <c:if test="${empty tasks}">
        <tr>
          <td colspan="6" class="empty-row">No tasks found.</td>
        </tr>
      </c:if>
      </tbody>
    </table>

    <!-- Pagination -->
    <div class="pagination">
      <c:forEach begin="1" end="${totalPages}" var="p">
        <a href="${pageContext.request.contextPath}/tasks?page=${p}&search=${search}&priority=${priorityFilter}&status=${statusFilter}&sortBy=${sortBy}&sortDir=${sortDir}"
           class="${p == currentPage ? 'active' : ''}">
            ${p}
        </a>
      </c:forEach>
    </div>
  </main>

</div>

</body>
</html>
