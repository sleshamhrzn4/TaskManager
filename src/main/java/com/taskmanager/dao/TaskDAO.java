package com.taskmanager.dao;

import com.taskmanager.model.TaskModel;
import com.taskmanager.utils.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    public void addTask(TaskModel tasks) throws Exception {
        Connection con = DBConfig.getConnection();
        String sql = "INSERT INTO tasks (userId, title, description, priority, status, createdDate, dueDate) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setInt(1, tasks.getUserId());
        pst.setString(2, tasks.getTitle());
        pst.setString(3, tasks.getDescription());
        pst.setString(4, tasks.getPriority());
        pst.setString(5, tasks.getStatus());
        pst.setTimestamp(6, Timestamp.valueOf(tasks.getCreatedDate()));

        if (tasks.getDueDate() != null) {
            pst.setDate(7, Date.valueOf(tasks.getDueDate()));
        } else {
            pst.setNull(7, Types.DATE);
        }

        pst.executeUpdate();

        pst.close();
        con.close();
    }


    private TaskModel mapRow(ResultSet rs) throws SQLException {
        TaskModel task = new TaskModel();
        task.setTaskId(rs.getInt("taskId"));
        task.setUserId(rs.getInt("userId"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setPriority(rs.getString("priority"));
        task.setStatus(rs.getString("status"));
        task.setCreatedDate(rs.getTimestamp("createdDate").toLocalDateTime());
        java.sql.Date dueDate = rs.getDate("dueDate");
        if (dueDate != null) {
            task.setDueDate(dueDate.toLocalDate());
        } else {
            task.setDueDate(null);
        }
        return task;
    }



     public List<TaskModel> getAllTaskByUser(int userId) throws Exception{
         List<TaskModel> taskList= new ArrayList<>();

         Connection con= DBConfig.getConnection();
         String sql= "SELECT * FROM tasks where UserId=?";
         PreparedStatement pst= con.prepareStatement(sql);
         pst.setInt(1,userId);

         ResultSet rs= pst.executeQuery();
         while (rs.next()){
             TaskModel task = mapRow(rs);
             taskList.add(task);
         }
         rs.close();
         pst.close();
         con.close();
         return taskList;
     }

     public TaskModel getTaskById(int taskId) throws Exception{
         TaskModel task=null;
         Connection con = DBConfig.getConnection();
         String sql = "SELECT * FROM tasks WHERE taskId=?";
         PreparedStatement pst= con.prepareStatement(sql);
         pst.setInt(1,taskId);

             ResultSet rs= pst.executeQuery();
             if(rs.next()){
                 task = mapRow(rs);

             }
             rs.close();
         pst.close();
         con.close();
         return task;
     }

        public List<TaskModel> searchTasks(int userId, String keyword) throws Exception {
            List<TaskModel> taskList = new ArrayList<>();
            String sql = "SELECT * FROM tasks WHERE userId = ? AND title LIKE ?";

            try (Connection con = DBConfig.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, userId);
                pst.setString(2, "%" + keyword + "%");

                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    taskList.add(mapRow(rs));
                }
            }
            return taskList;
        }

     public int updateTask(TaskModel tasks) throws Exception{
         Connection con = DBConfig.getConnection();
         String sql = "UPDATE tasks SET title=?, description=?, priority=?, status=?, createdDate=?, dueDate=? WHERE taskId=?";
         PreparedStatement pst = con.prepareStatement(sql);

         pst.setString(1, tasks.getTitle());
         pst.setString(2, tasks.getDescription());
         pst.setString(3, tasks.getPriority());
         pst.setString(4, tasks.getStatus());
         pst.setTimestamp(5,Timestamp.valueOf(tasks.getCreatedDate()));
        pst.setDate(6, Date.valueOf(tasks.getDueDate()));
         pst.setInt(7, tasks.getTaskId());

         int rowsAffected = pst.executeUpdate();
         pst.close();
         con.close();
        return rowsAffected;

     }


     public void deleteTask(int taskId) throws Exception{
         Connection con = DBConfig.getConnection();
         String sql= "DELETE FROM tasks WHERE taskId=?";
         PreparedStatement pst=con.prepareStatement(sql);

         pst.setInt(1,taskId);
         pst.executeUpdate();

         pst.close();
         con.close();


     }


}
