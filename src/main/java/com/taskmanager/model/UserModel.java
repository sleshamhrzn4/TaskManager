package com.taskmanager.model;

public class UserModel {
    private int userId;
    private String userName;
    private String userEmail;
    private String password;
    private String role;

    public UserModel(int userId, String userName, String userEmail, String password,
                     String role) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.role = role;

    }


    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;

    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }

    public String getRo() {
        return role;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(){
        this.userName= userName;
    }

    public void setUserEmail(){
        this.userEmail= userEmail;
    }

    public void setPassword(){
        this.password= password;
    }

    public void setRole(){
        this.role=role;
    }

}


