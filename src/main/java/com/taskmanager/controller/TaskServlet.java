package com.taskmanager.controller;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.TaskModel;
import com.taskmanager.model.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@WebServlet(asyncSupported = true, urlPatterns = {"/task"})
public class TaskServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel user = getLoggedInUser(req, resp);
        if (user == null) return;

        try {
            TaskDAO taskDAO = new TaskDAO();
            List<TaskModel> allTasks = taskDAO.getAllTaskByUser(user.getUserId());

            req.setAttribute("todoTasks", filterByStatus(allTasks, "todo"));
            req.setAttribute("progressTasks", filterByStatus(allTasks, "inprogress"));
            req.setAttribute("doneTasks", filterByStatus(allTasks, "done"));

            req.getRequestDispatcher("/WEB-INF/pages/task.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Could not load tasks.");
            req.getRequestDispatcher("/WEB-INF/pages/task.jsp").forward(req, resp);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel user = getLoggedInUser(req, resp);
        if (user == null) return; // already redirected to login

        String action = req.getParameter("action");
        String errorMessage = null;

        if (action == null) {
            errorMessage = "Missing action.";
        } else {
            try {
                switch (action) {
                    case "add":
                        errorMessage = handleAdd(req, user);
                        break;
                    case "update":
                        errorMessage = handleUpdate(req);
                        break;
                    case "delete":
                        errorMessage = handleDelete(req);
                        break;
                    case "move":
                        errorMessage = handleMove(req);
                        break;
                    default:
                        errorMessage = "Unknown action: " + action;
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = "Something went wrong. Please try again.";
            }
        }

        if (errorMessage != null) {
            req.setAttribute("errorMessage", errorMessage);
            doGet(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/task");
    }


    private String handleAdd(HttpServletRequest req, UserModel user) throws Exception {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String priority = req.getParameter("priority");
        String dueDateStr = req.getParameter("dueDate");

        String titleError = validateTitle(title);
        if (titleError != null) return titleError;

        String priorityError = validatePriority(priority);
        if (priorityError != null) return priorityError;

        LocalDate dueDate;
        try {
            dueDate = parseDueDate(dueDateStr);
        } catch (Exception e) {
            return "Invalid due date format.";
        }

        TaskModel task = new TaskModel();
        task.setUserId(user.getUserId());
        task.setTitle(title.trim());
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus("todo");
        task.setCreatedDate(LocalDateTime.now());
        task.setDueDate(dueDate);

        new TaskDAO().addTask(task);
        return null; // success
    }

    private String handleUpdate(HttpServletRequest req) throws Exception {
        String taskIdStr = req.getParameter("taskId");
        if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
            return "taskId is required for update.";
        }
        int taskId = Integer.parseInt(taskIdStr);

        TaskDAO taskDAO = new TaskDAO();
        TaskModel existing = taskDAO.getTaskById(taskId);
        if (existing == null) {
            return "Task not found.";
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String priority = req.getParameter("priority");
        String status = req.getParameter("status");
        String dueDateStr = req.getParameter("dueDate");

        String titleError = validateTitle(title);
        if (titleError != null) return titleError;

        String priorityError = validatePriority(priority);
        if (priorityError != null) return priorityError;

        String statusError = validateStatus(status);
        if (statusError != null) return statusError;

        LocalDate dueDate;
        try {
            dueDate = parseDueDate(dueDateStr);
        } catch (Exception e) {
            return "Invalid due date format.";
        }

        existing.setTitle(title.trim());
        existing.setDescription(description);
        existing.setPriority(priority);
        existing.setStatus(status);
        existing.setDueDate(dueDate);

        taskDAO.updateTask(existing);
        return null;
    }

    private String handleDelete(HttpServletRequest req) throws Exception {
        String taskIdStr = req.getParameter("taskId");
        if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
            return "taskId is required for delete.";
        }
        int taskId = Integer.parseInt(taskIdStr);
        new TaskDAO().deleteTask(taskId);
        return null;
    }

    private String handleMove(HttpServletRequest req) throws Exception {
        String taskIdStr = req.getParameter("taskId");
        String status = req.getParameter("status");

        if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
            return "taskId is required.";
        }

        String statusError = validateStatus(status);
        if (statusError != null) return statusError;

        int taskId = Integer.parseInt(taskIdStr);
        TaskDAO taskDAO = new TaskDAO();
        TaskModel existing = taskDAO.getTaskById(taskId);
        if (existing == null) {
            return "Task not found.";
        }

        existing.setStatus(status);
        taskDAO.updateTask(existing);
        return null;
    }

    private UserModel getLoggedInUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        return (UserModel) session.getAttribute("user");
    }

    private List<TaskModel> filterByStatus(List<TaskModel> tasks, String status) {
        return tasks.stream()
                .filter(t -> status.equalsIgnoreCase(t.getStatus()))
                .toList();
    }


    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "Title is required.";
        }
        if (title.length() > 255) {
            return "Title is too long (max 255 characters).";
        }
        return null;
    }

    private String validatePriority(String priority) {
        if (priority == null ||
                !(priority.equalsIgnoreCase("low")
                        || priority.equalsIgnoreCase("medium")
                        || priority.equalsIgnoreCase("high"))) {
            return "Priority must be low, medium, or high.";
        }
        return null;
    }

    private String validateStatus(String status) {
        if (status == null ||
                !(status.equalsIgnoreCase("todo")
                        || status.equalsIgnoreCase("inprogress")
                        || status.equalsIgnoreCase("done"))) {
            return "Status must be todo, inprogress, or done.";
        }
        return null;
    }

    private LocalDate parseDueDate(String dueDateStr) {
        if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dueDateStr);
    }
}