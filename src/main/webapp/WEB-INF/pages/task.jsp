<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>TEGER | My Tasks</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tasks.css">
</head>
<body>

<div class="app-layout">

    <aside class="sidebar">
        <div class="sidebar-brand"><a href="${pageContext.request.contextPath}/dashboard" class="nav-item">
            <img src="${pageContext.request.contextPath}/tegar.png"  alt="dashboard" style="width: 200px; height: auto;">

        </a>
        </div>

        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/task" class="active">My Tasks</a>
        </nav>

        <a href="${pageContext.request.contextPath}/logout" class="logout-link">Log out</a>
    </aside>

    <main class="main-content">

        <div class="page-header">
            <h1>My Tasks</h1>
            <button type="button" class="add-task-toggle" onclick="toggleAddForm()">+ Add Task</button>
        </div>

        <div class="search-form">
            <input type="text" id="liveSearchInput" placeholder="Search tasks..." oninput="liveSearch(this.value)">

        </div>


        <form id="addTaskForm" action="$pageContext.request.contextPath}/task" method="post" class="add-task-form" >
            <input type="hidden" name="action" value="add"/>
            <label>
                <input type="text" name="title" placeholder="Task title" required/>
            </label>
            <label>
                <input type = "text" name="description" placeholder="Description">
            </label>
            <label>
                <select>
                    <option value="Low"></option>
                    <option value="Medium"></option>
                    <option value="HIgh"></option>
                </select>
            </label>
            <input type="date" name="dueDate"/>
            <button type="submit">Add Task</button>
            <button type="button" class="cancel-btn"></button>


        </form>


        <hr/>

        <div class="board">

            <!-- TO DO -->
            <div class="column">
                <div class="column-header">
                    <h2>To Do</h2>
                    <span class="count">${fn:length(todoTasks)}</span>
                </div>
                <c:forEach var="task" items="${todoTasks}">
                    <div class="card"
                         data-id="${task.taskId}"
                         data-title="${task.title}"
                         data-description="${task.description}"
                         data-priority="${task.priority}"
                         data-duedate="${task.dueDate}"
                         data-status="${task.status}"
                         onclick="openTaskModal(this)">
                        <span class="dot dot-${fn:toLowerCase(task.priority)}"></span>
                            ${task.title}
                    </div>
                </c:forEach>
            </div>

            <div class="column">
                <div class="column-header">
                    <h2>In Progress</h2>
                    <span class="count">${fn:length(progressTasks)}</span>
                </div>
                <c:forEach var="task" items="${progressTasks}">
                    <div class="card"
                         data-id="${task.taskId}"
                         data-title="${task.title}"
                         data-description="${task.description}"
                         data-priority="${task.priority}"
                         data-duedate="${task.dueDate}"
                         data-status="${task.status}"
                         onclick="openTaskModal(this)">
                        <span class="dot dot-${fn:toLowerCase(task.priority)}"></span>
                            ${task.title}
                    </div>
                </c:forEach>
            </div>


            <div class="column">
                <div class="column-header">
                    <h2>Done</h2>
                    <span class="count">${fn:length(doneTasks)}</span>
                </div>
                <c:forEach var="task" items="${doneTasks}">
                    <div class="card card-done"
                         data-id="${task.taskId}"
                         data-title="${task.title}"
                         data-description="${task.description}"
                         data-priority="${task.priority}"
                         data-duedate="${task.dueDate}"
                         data-status="${task.status}"
                         onclick="openTaskModal(this)">
                        <span class="dot dot-${fn:toLowerCase(task.priority)}"></span>
                            ${task.title}
                    </div>
                </c:forEach>
            </div>

    </main>
</div>


<div id="taskOverlay" class="modal-overlay">
    <div class="modal">
        <div class="modal-header">
            <h2>Task Details</h2>
            <button type="button" class="close-btn" onclick="closeTaskModal()">&times;</button>
        </div>

        <form action="${pageContext.request.contextPath}/task" method="post">
            <input type="hidden" name="action" value="update"/>
            <input type="hidden" id="modalTaskId" name="taskId"/>

            <label>
                Title
                <input type="text" id="modalTitle" name="title" required/>
            </label>

            <label>
                Description
                <input type="text" id="modalDescription" name="description"/>
            </label>

            <label>
                Priority
                <select id="modalPriority" name="priority">
                    <option value="Low">Low</option>
                    <option value="Medium">Medium</option>
                    <option value="High">High</option>
                </select>
            </label>

            <label>
                Status
                <select id="modalStatus" name="status">
                    <option value="todo">To Do</option>
                    <option value="inprogress">In Progress</option>
                    <option value="done">Done</option>
                </select>
            </label>

            <label>
                Due date
                <input type="date" id="modalDueDate" name="dueDate"/>
            </label>

            <div class="modal-actions">
                <button type="submit" class="save-btn">Save</button>
            </div>
        </form>

        <form action="${pageContext.request.contextPath}/task" method="post" class="delete-form">
            <input type="hidden" name="action" value="delete"/>
            <input type="hidden" id="modalDeleteTaskId" name="taskId"/>
            <button type="submit" class="delete-btn">Delete Task</button>
        </form>
    </div>
</div>

<script>
    function toggleAddForm() {
        document.getElementById('addTaskForm').classList.toggle('open');
    }

    function openTaskModal(card) {
        document.getElementById('modalTaskId').value = card.dataset.id;
        document.getElementById('modalDeleteTaskId').value = card.dataset.id;
        document.getElementById('modalTitle').value = card.dataset.title;
        document.getElementById('modalDescription').value = card.dataset.description;
        document.getElementById('modalPriority').value = card.dataset.priority;
        document.getElementById('modalStatus').value = card.dataset.status;
        document.getElementById('modalDueDate').value = card.dataset.duedate;

        document.getElementById('taskOverlay').classList.add('open');
    }

    function closeTaskModal() {
        document.getElementById('taskOverlay').classList.remove('open');
    }

    function liveSearch(query){
        var lowerQuery = query.toLowerCase();
        var cards = document.querySelectorAll('.card');

        cards.forEach(function(card){
            var title = card.dataset.title.toLowerCase();
            if (title.includes(lowerQuery)){
                card.style.display = '';
            }else {
                card.style.display = 'none';
            }

        });

    }
</script>

</body>
</html>
