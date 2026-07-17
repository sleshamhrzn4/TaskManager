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

    public UserModel() {

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

    public String getRole() {
        return role;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName){

        this.userName= userName;
    }

    public void setUserEmail(String userEmail){

        this.userEmail= userEmail;
    }

    public void setPassword(String password){

        this.password= password;
    }

    public void setRole(String role){
        this.role= role;
    }

}


