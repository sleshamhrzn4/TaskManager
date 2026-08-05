package com.taskmanager.controller;

import com.taskmanager.exception.ValidationException;
import com.taskmanager.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.Serial;

@WebServlet(asyncSupported = true, urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    @Serial
    public static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String userEmail = req.getParameter("userEmail");
        String password = req.getParameter("password");
        long organizationId = 1L;

        UserService userService = new UserService();

        try {
            userService.registerUser(userName, userEmail, password, organizationId);
            resp.sendRedirect(req.getContextPath() + "/login");

        } catch (ValidationException e) {
            if (e.getFieldErrors() != null) {
                req.setAttribute("errors", e.getFieldErrors());
            } else {
                req.setAttribute("errorMessage", e.getMessage());
            }
            req.setAttribute("oldUserName", userName);
            req.setAttribute("oldUserEmail", userEmail);
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Registration failed. Please try again.");
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
        }
    }
    }
