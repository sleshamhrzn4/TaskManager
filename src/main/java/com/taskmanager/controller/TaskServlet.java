package com.taskmanager.controller;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.TaskModel;
import com.taskmanager.model.UserModel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/task")
public class TaskServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();

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
                    handleAdd(request, user.getUserId());
                    break;
                case "update":
                    handleUpdate(request);
                    break;
                case "delete":
                    handleDelete(request);
                    break;
                default:
                    break;
            }
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

    private void handleAdd(HttpServletRequest request, int userId) throws Exception {
        TaskModel task = new TaskModel();
        task.setUserId(userId);
        task.setTitle(request.getParameter("title"));
        task.setDescription(request.getParameter("description"));
        task.setPriority(request.getParameter("priority"));
        task.setStatus("todo");
        task.setCreatedDate(LocalDateTime.now());

        String dueDateStr = request.getParameter("dueDate");
        task.setDueDate((dueDateStr != null && !dueDateStr.trim().isEmpty())
                ? LocalDate.parse(dueDateStr) : null);

        taskDAO.addTask(task);
    }

    private void handleUpdate(HttpServletRequest request) throws Exception {
        int taskId = Integer.parseInt(request.getParameter("taskId"));

        TaskModel task = taskDAO.getTaskById(taskId);
        if (task == null) return;

        task.setTitle(request.getParameter("title"));
        task.setDescription(request.getParameter("description"));
        task.setPriority(request.getParameter("priority"));
        task.setStatus(request.getParameter("status"));

        String dueDateStr = request.getParameter("dueDate");
        task.setDueDate((dueDateStr != null && !dueDateStr.trim().isEmpty())
                ? LocalDate.parse(dueDateStr) : null);

        taskDAO.updateTask(task);
    }

    private void handleDelete(HttpServletRequest request) throws Exception {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        taskDAO.deleteTask(taskId);
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
            List<TaskModel> allTasks = taskDAO.getAllTaskByUser(user.getUserId());
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