package com.taskmanager.controller;

import com.taskmanager.model.UserModel;
import com.taskmanager.service.LoginService;
import
        jakarta.servlet.ServletException;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Email is required.");
            doGet(req, resp);
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Password is required.");
            doGet(req, resp);

        }

        email= email.trim();

        try{
            LoginService loginService = new LoginService();
            UserModel user = loginService.authentiate(email, password);

            if (user == null) {
                req.setAttribute("errorMessage", "Invalid email or password.");
                doGet(req, resp);
                return;

            }
        }catch (Exception e ){
            e.printStackTrace();
            req.setAttribute("errorMessage", "Something is wrong.Please try again later.");
        }
}
}