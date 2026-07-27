<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:if test="${not empty errorMessage}">
    <div class="error-msg">${errorMessage}</div>
</c:if>

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
            <td>${highlightedTitles[task.taskId]}</td>
            <td>${highlightedDescriptions[task.taskId]}</td>
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

<div class="pagination">
    <c:forEach begin="1" end="${totalPages}" var="p">
        <a href="${pageContext.request.contextPath}/tasklist?page=${p}&search=${search}&priority=${priorityFilter}&status=${statusFilter}&sortBy=${sortBy}&sortDir=${sortDir}&overdue=${overdueOnly}"
           class="${p == currentPage ? 'active' : ''}">
                ${p}
        </a>
    </c:forEach>
</div>