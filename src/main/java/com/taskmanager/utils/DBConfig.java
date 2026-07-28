package com.taskmanager.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConfig {
    private static final String URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/taskmanager");
    private static final String USER = System.getProperty("db.user", "root");
    private static final String PASSWORD= System.getProperty("db.password", "");
    private static final String DRIVER =System.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    public static Connection getConnection(){
        Connection conn= null ;
        try {


            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Connection to DB");
        }catch (Exception e){
            System.out.println("DB Connection Failed");
            e.printStackTrace();
        }
        return conn;
    }
}
