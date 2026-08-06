package com.taskmanager.controller;

import com.taskmanager.exception.ValidationException;
import com.taskmanager.model.UserModel;
import com.taskmanager.service.UserService;
import com.taskmanager.utils.CsrfUtil;
import
        jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

        UserService userService = new UserService();

        try {
            UserModel user = userService.authenticateUser(email, password);

            HttpSession session = req.getSession(true);
            session.invalidate();
            session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("csrfToken", CsrfUtil.generateToken());

            resp.sendRedirect(req.getContextPath() + "/dashboard");

        } catch (ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            doGet(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Something is wrong. Please try again later.");
            doGet(req, resp);
        }
    }
}
