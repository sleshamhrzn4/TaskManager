package com.taskmanager.service;


import java.util.HashMap;
import java.util.Map;

public class RegisterService {


    public Map<String,String> validate(String userName, String userEmail, String password){
        Map<String , String> errors = new HashMap<>();

        if (userName == null || userName.trim().isEmpty()){
            errors.put("userName", "Username is required.");

        }
        if (userEmail == null || userEmail.trim().isEmpty()) {
            errors.put("email", "Email is required.");
        } else if (!userEmail.trim().toLowerCase().endsWith("@gmail.com")) {
            errors.put("userEmail", "Email must be a valid @gmail.com address.");
        }

        // Password
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Password is required.");
        } else if (password.length() < 6) {
            errors.put("password", "Password must be at least 6 characters.");
        }

        return errors;


    }

}
