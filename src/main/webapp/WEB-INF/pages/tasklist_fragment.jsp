<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:if test="${not empty errorMessage}">
    <div class="error-msg"><c:out value="${errorMessage}"/></div>
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
        <c:url var="pageUrl" value="/tasklist">
            <c:param name="page" value="${p}"/>
            <c:param name="search" value="${search}"/>
            <c:param name="priority" value="${priorityFilter}"/>
            <c:param name="status" value="${statusFilter}"/>
            <c:param name="sortBy" value="${sortBy}"/>
            <c:param name="sortDir" value="${sortDir}"/>
            <c:param name="overdue" value="${overdueOnly}"/>
        </c:url>
        <a href="${pageUrl}" class="${p == currentPage ? 'active' : ''}">${p}</a>
    </c:forEach>
</div>