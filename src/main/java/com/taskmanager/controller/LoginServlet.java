package com.taskmanager.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

@WebServlet(asyncSupported = true,urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoginServlet(){
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req,resp);
    }
}
