package com.taskmanager.dao;

import com.taskmanager.model.UserModel;
import com.taskmanager.utils.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class UserDAO {

    public void insertUser( String userName, String userEmail, String password, String role)
    throws Exception{
        Connection con= DBConfig.getConnection();

        String sql = "INSERT INTO users ( userName, userEmail, password, role) " + "VALUES (?,?,?,?)";

        PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1,userName);
        pst.setString(2,userEmail);
        pst.setString(3,password);
        pst.setString(4,"user");

        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()){
            System.out.println("New user inserted with ID:" + rs.getInt(1));
        }
rs.close();
        pst.close();
        con.close();

    }


    public UserModel getUserByEmail(String email)
        throws Exception{
        UserModel user= null;
        Connection con = DBConfig.getConnection();
        String sql ="SELECT * FROM users WHERE userEmail=?";

        PreparedStatement pst= con.prepareStatement(sql);
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();

        if(rs.next()){
            user = new UserModel();
            user.setUserId(rs.getInt("userId"));
            user.setUserName(rs.getString("userName"));
            user.setUserEmail(rs.getString("userEmail"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));



        }
        rs.close();
        pst.close();
        con.close();
        return user;

    }
}
