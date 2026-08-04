package com.taskmanager.controller;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.TaskModel;
import com.taskmanager.model.UserModel;
import com.taskmanager.service.TaskService;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.UnauthorizedAccessException;
import com.taskmanager.exception.ValidationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/task")
public class TaskServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();
    private final TaskService taskService = new TaskService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadBoard(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserModel user = getSessionUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "";

        try {
            switch (action) {
                case "add":
                    handleAdd(request, user.getUserId(), user.getWorkspaceId());
                    break;
                case "update":
                    handleUpdate(request, user.getWorkspaceId());
                    break;
                case "delete":
                    handleDelete(request, user.getWorkspaceId());
                    break;
                default:
                    break;
            }
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
        } catch (ResourceNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return;
        } catch (UnauthorizedAccessException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Something went wrong: " + e.getMessage());
        }

        loadBoard(request, response);
    }

    private UserModel getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (UserModel) session.getAttribute("user");
    }

    private void handleAdd(HttpServletRequest request, int userId, int workspaceId) throws Exception {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");

        String dueDateStr = request.getParameter("dueDate");
        LocalDate dueDate = (dueDateStr != null && !dueDateStr.trim().isEmpty())
                ? LocalDate.parse(dueDateStr) : null;

        taskService.createTask(userId, workspaceId, title, description, priority, dueDate);
    }

    private void handleUpdate(HttpServletRequest request, int workspaceId) throws Exception {
        int taskId = Integer.parseInt(request.getParameter("taskId"));

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");

        String dueDateStr = request.getParameter("dueDate");
        LocalDate dueDate = (dueDateStr != null && !dueDateStr.trim().isEmpty())
                ? LocalDate.parse(dueDateStr) : null;

        taskService.updateTask(taskId, workspaceId, title, description, priority, status, dueDate);
    }

    private void handleDelete(HttpServletRequest request, int workspaceId) throws Exception {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        taskService.deleteTask(taskId, workspaceId);
    }

    private void loadBoard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserModel user = getSessionUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<TaskModel> todoTasks = new ArrayList<>();
        List<TaskModel> progressTasks = new ArrayList<>();
        List<TaskModel> doneTasks = new ArrayList<>();

        try {
            List<TaskModel> allTasks = taskDAO.getAllTaskByUser(user.getUserId(), user.getWorkspaceId());
            for (TaskModel task : allTasks) {
                switch (task.getStatus()) {
                    case "todo":
                        todoTasks.add(task);
                        break;
                    case "inprogress":
                        progressTasks.add(task);
                        break;
                    case "done":
                        doneTasks.add(task);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Could not load tasks: " + e.getMessage());
        }

        request.setAttribute("todoTasks", todoTasks);
        request.setAttribute("progressTasks", progressTasks);
        request.setAttribute("doneTasks", doneTasks);

        request.getRequestDispatcher("/WEB-INF/pages/task.jsp").forward(request, response);
    }
}