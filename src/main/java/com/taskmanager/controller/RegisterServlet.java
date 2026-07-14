package com.taskmanager.controller;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.service.RegisterService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.Map;

@WebServlet(asyncSupported = true, urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet  {
    @Serial
    public static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName= req.getParameter("userName");
        String userEmail= req.getParameter("userEmail");
        String password= req.getParameter("password");


        RegisterService service=new RegisterService();

        Map<String,String> errors= service.validate(userName, userEmail,password);

        if (!errors.isEmpty()){
            req.setAttribute("errors", errors);

            req.setAttribute("oldUserName", userName);
            req.setAttribute("oldUserEmail", userEmail);

            req.getRequestDispatcher("/WEB-INF/pages/register.jsp")
                    .forward(req, resp);
            return;

        }

        try{
            UserDAO userDAO=new UserDAO();
            userDAO.insertUser(userName, userEmail, password, "user");
            resp.sendRedirect(req.getContextPath()+"/login");
        }catch(Exception e){
            e.printStackTrace();
            req.setAttribute("errorMessage", "Registration failed. Please try again.");
            req.getRequestDispatcher("/WEB-INF/pages/public/register.jsp")
                    .forward(req, resp);

        }



    }
}
