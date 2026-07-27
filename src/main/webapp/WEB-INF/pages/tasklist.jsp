<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
  <title>TEGER | All Tasks</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tasks.css">
  <style>
    mark { background: #fff3a3; padding: 0 2px; border-radius: 2px; }
  </style>
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
    <form action="${pageContext.request.contextPath}/tasklist" method="get" class="list-controls">
      <input type="text" name="search" placeholder="Search title or description..." value="${search}"/>

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

      <label>
        <input type="checkbox" name="overdue" value="true" ${overdueOnly ? 'checked' : ''} />
        Overdue only
      </label>

      <button type="submit">Apply</button>

      <a href="${pageContext.request.contextPath}/tasklist" id="clearSearchLink" class="clear-search-link" style="display:none;">✕ Clear</a>
    </form>

    <div id="taskListContainer">
      <jsp:include page="tasklist_fragment.jsp"/>

    </div>
  </main>

</div>

<script>
  (function () {
    const searchInput = document.getElementById('searchInput');
    const form = document.getElementById('listControlsForm');
    const container = document.getElementById('taskListContainer');
    const clearLink = document.getElementById('clearSearchLink');
    const contextPath = "${pageContext.request.contextPath}";

    let debounceTimer = null;
    let currentRequestId = 0;

    function updateClearLinkVisibility() {
      clearLink.style.display = searchInput.value.trim() ? 'inline' : 'none';
    }

    function buildQuery() {
      const formData = new FormData(form);
      const params = new URLSearchParams();
      for (const [key, value] of formData.entries()) {
        params.append(key, value);
      }
      return params.toString();
    }

    function runSearch() {
      const requestId = ++currentRequestId;
      const query = buildQuery();

      fetch(contextPath + '/tasklist?' + query, {
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
      })
              .then(function (resp) {
                if (!resp.ok) throw new Error('Request failed: ' + resp.status);
                return resp.text();
              })
              .then(function (html) {
                if (requestId !== currentRequestId) return;
                container.innerHTML = html;
                history.replaceState(null, '', contextPath + '/tasklist?' + query);
              })
              .catch(function (err) {
                console.error('Live search failed:', err);
              });
    }

    searchInput.addEventListener('input', function () {
      updateClearLinkVisibility();
      clearTimeout(debounceTimer);
      debounceTimer = setTimeout(runSearch, 300);
    });


    updateClearLinkVisibility();
  })();
</script>

</body>
</html>
