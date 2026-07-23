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
import java.util.List;

@WebServlet(urlPatterns = {"/tasks"})
public class TaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        UserModel user = (UserModel) session.getAttribute("user");

        try {
            String search = req.getParameter("search");
            String priorityFilter = req.getParameter("priority");
            String statusFilter = req.getParameter("status");
            String sortBy = req.getParameter("sortBy");
            String sortDir = req.getParameter("sortDir");

            if (sortBy == null) sortBy = "dueDate";
            if (sortDir == null) sortDir = "ASC";
            if (priorityFilter == null) priorityFilter = "all";
            if (statusFilter == null) statusFilter = "all";

            int pageNumber = 1;
            String pageParam = req.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    pageNumber = Integer.parseInt(pageParam);
                    if (pageNumber < 1) pageNumber = 1;
                } catch (NumberFormatException ignored) {
                    pageNumber = 1;
                }
            }
            int pageSize = 5;

            TaskDAO taskDAO = new TaskDAO();
            List<TaskModel> tasks = taskDAO.getTasksPaged(
                    user.getUserId(), search, priorityFilter, statusFilter,
                    sortBy, sortDir, pageNumber, pageSize);

            int totalCount = taskDAO.getTotalTaskCount(user.getUserId(), search, priorityFilter, statusFilter);
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);
            if (totalPages < 1) totalPages = 1;

            req.setAttribute("tasks", tasks);
            req.setAttribute("currentPage", pageNumber);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("search", search);
            req.setAttribute("priorityFilter", priorityFilter);
            req.setAttribute("statusFilter", statusFilter);
            req.setAttribute("sortBy", sortBy);
            req.setAttribute("sortDir", sortDir);

            req.getRequestDispatcher("/WEB-INF/pages/task-list.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Could not load tasks.");
            req.getRequestDispatcher("/WEB-INF/pages/task-list.jsp").forward(req, resp);
        }
    }
}