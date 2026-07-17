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
import java.time.LocalDate;
import java.util.List;

@WebServlet(asyncSupported = true,urlPatterns = {"/dashboard"})
    public class DashboardServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            UserModel user = (UserModel) session.getAttribute("user");

            try {
                TaskDAO taskDAO = new TaskDAO();
                List<TaskModel> allTasks = taskDAO.getAllTaskByUser(user.getUserId());

                int total = allTasks.size();
                int completed = 0, pending = 0, overdue = 0;

                for (TaskModel t : allTasks) {
                    if ("done".equalsIgnoreCase(t.getStatus())) {
                        completed++;
                    } else {
                        pending++;
                        if (t.getDueDate() != null && t.getDueDate().isBefore(LocalDate.now())) {
                            overdue++;
                        }
                    }
                }

                req.setAttribute("totalCount", total);
                req.setAttribute("completedCount", completed);
                req.setAttribute("pendingCount", pending);
                req.setAttribute("overdueCount", overdue);

                req.getRequestDispatcher("/WEB-INF/pages/dashboard.jsp").forward(req, resp);

            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("errorMessage", "Could not load dashboard.");
                req.getRequestDispatcher("/WEB-INF/pages/dashboard.jsp").forward(req, resp);
            }
        }
    }

