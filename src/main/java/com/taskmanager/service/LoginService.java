package com.taskmanager.service;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.model.UserModel;
import com.taskmanager.utils.PasswordUtil;

import javax.naming.AuthenticationException;
import java.net.PasswordAuthentication;

public class LoginService {
    public UserModel authentiate(String email, String password) throws Exception{
        UserDAO userDAO = new UserDAO();
        UserModel user = userDAO.getUserByEmail(email);

        if (email == null ){
            return null;
        }

        boolean passwordMatch = PasswordUtil.checkPassword(password,user.getPassword());
        if(!passwordMatch){
            return null;
        }
        return user;
    }


}
