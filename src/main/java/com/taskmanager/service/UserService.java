package com.taskmanager.service;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.model.UserModel;
import com.taskmanager.exception.ValidationException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.utils.PasswordUtil;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public void registerUser(String userName, String userEmail, String password, long organizationId) throws Exception {
        Map<String, String> errors = new HashMap<>();

        if (userName == null || userName.trim().isEmpty()) {
            errors.put("userName", "Username is required.");
        }
        if (userEmail == null || userEmail.trim().isEmpty()) {
            errors.put("userEmail", "Email is required.");
        } else if (!userEmail.trim().toLowerCase().endsWith("@gmail.com")) {
            errors.put("userEmail", "Email must be a valid @gmail.com address.");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Password is required.");
        } else if (password.length() < 6) {
            errors.put("password", "Password must be at least 6 characters.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        if (userDAO.existsByEmail(userEmail.trim())) {
            errors.put("userEmail", "An account with this email already exists.");
            throw new ValidationException(errors);
        }

        String hashedPassword = PasswordUtil.getHashPassword(password);
        userDAO.insertUser(userName, userEmail.trim(), hashedPassword, "user", organizationId);
    }

    public UserModel authenticateUser(String email, String password) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email is required.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password is required.");
        }

        UserModel user = userDAO.getUserByEmail(email.trim());
        if (user == null) {
            throw new ValidationException("Invalid email or password.");
        }

        boolean passwordMatch = PasswordUtil.checkPassword(password, user.getPassword());
        if (!passwordMatch) {
            throw new ValidationException("Invalid email or password.");
        }

        return user;
    }

    public UserModel getUserById(int userId) throws Exception {
        UserModel user = userDAO.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return user;
    }
}