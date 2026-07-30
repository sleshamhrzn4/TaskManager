package com.taskmanager.dao;

import com.taskmanager.model.TaskModel;
import com.taskmanager.utils.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    public void addTask(TaskModel tasks) throws Exception {
        Connection con = DBConfig.getConnection();
        String sql = "INSERT INTO tasks (userId,workspaceId, title, description, priority, status, createdDate, dueDate) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setInt(1, tasks.getUserId());
        pst.setInt(2,tasks.getWorkspaceId());
        pst.setString(3, tasks.getTitle());
        pst.setString(4, tasks.getDescription());
        pst.setString(5, tasks.getPriority());
        pst.setString(6, tasks.getStatus());
        pst.setTimestamp(7, Timestamp.valueOf(tasks.getCreatedDate()));

        if (tasks.getDueDate() != null) {
            pst.setDate(8, Date.valueOf(tasks.getDueDate()));
        } else {
            pst.setNull(8, Types.DATE);
        }

        pst.executeUpdate();

        pst.close();
        con.close();
    }


    private TaskModel mapRow(ResultSet rs) throws SQLException {
        TaskModel task = new TaskModel();
        task.setTaskId(rs.getInt("taskId"));
        task.setUserId(rs.getInt("userId"));
        task.setWorkspaceId(rs.getInt("workspaceId"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setPriority(rs.getString("priority"));
        task.setStatus(rs.getString("status"));
        task.setCreatedDate(rs.getTimestamp("createdDate").toLocalDateTime());
        Date dueDate = rs.getDate("dueDate");
        if (dueDate != null) {
            task.setDueDate(dueDate.toLocalDate());
        } else {
            task.setDueDate(null);
        }
        return task;
    }



     public List<TaskModel> getAllTaskByUser(int userId,int workspaceId) throws Exception{
         List<TaskModel> taskList= new ArrayList<>();

         Connection con= DBConfig.getConnection();
         String sql= "SELECT * FROM tasks where UserId=? AND workspaceId=?";
         PreparedStatement pst= con.prepareStatement(sql);
         pst.setInt(1,userId);
         pst.setInt(2,workspaceId);

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

        public List<TaskModel> searchTasks(int userId,int workspaceId, String keyword) throws Exception {
            List<TaskModel> taskList = new ArrayList<>();
            String sql = "SELECT * FROM tasks WHERE userId = ? AND workspaceId=? AND title LIKE ?";

            try (Connection con = DBConfig.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, userId);
                pst.setInt(2,workspaceId);
                pst.setString(3, "%" + keyword + "%");

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

    public List<TaskModel> getTasksPaged(int userId, int workspaceId, String search, String priorityFilter,
                                         String statusFilter, boolean overdueOnly,
                                         String sortBy, String sortDir,
                                         int pageNumber, int pageSize) throws Exception {

        List<TaskModel> taskList = new ArrayList<>();

        String column;
        switch (sortBy) {
            case "priority": column = "priority"; break;
            case "createdDate": column = "createdDate"; break;
            case "title": column = "title"; break;
            case "dueDate": default: column = "dueDate"; break;
        }

        String direction = "DESC".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";

        StringBuilder sql = new StringBuilder("SELECT * FROM tasks WHERE userId = ? AND workspaceId=?");
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(workspaceId);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (title LIKE ? OR description LIKE ?)");
            String likeTerm = "%" + search.trim() + "%";
            params.add(likeTerm);
            params.add(likeTerm);
        }

        if (priorityFilter != null && !priorityFilter.equalsIgnoreCase("all")) {
            sql.append(" AND priority = ?");
            params.add(priorityFilter);
        }

        if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
            sql.append(" AND status = ?");
            params.add(statusFilter);
        }

        if (overdueOnly) {
            sql.append(" AND dueDate < CURRENT_DATE AND status != 'done'");
        }

        sql.append(" ORDER BY ").append(column).append(" ").append(direction);
        sql.append(" LIMIT ? OFFSET ?");

        int offset = (pageNumber - 1) * pageSize;

        Connection con = DBConfig.getConnection();
        PreparedStatement pst = con.prepareStatement(sql.toString());

        int i = 1;
        for (Object param : params) {
            pst.setObject(i++, param);
        }
        pst.setInt(i++, pageSize);
        pst.setInt(i, offset);

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            taskList.add(mapRow(rs));
        }

        rs.close();
        pst.close();
        con.close();

        return taskList;
    }

    public int getTotalTaskCount(int userId,int workspaceId, String search, String priorityFilter,
                                 String statusFilter, boolean overdueOnly) throws Exception {

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM tasks WHERE userId = ? AND workspaceId=?");
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(workspaceId);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (title LIKE ? OR description LIKE ?)");
            String likeTerm = "%" + search.trim() + "%";
            params.add(likeTerm);
            params.add(likeTerm);
        }

        if (priorityFilter != null && !priorityFilter.equalsIgnoreCase("all")) {
            sql.append(" AND priority = ?");
            params.add(priorityFilter);
        }

        if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
            sql.append(" AND status = ?");
            params.add(statusFilter);
        }

        if (overdueOnly) {
            sql.append(" AND dueDate < CURRENT_DATE AND status != 'done'");
        }

        Connection con = DBConfig.getConnection();
        PreparedStatement pst = con.prepareStatement(sql.toString());

        int i = 1;
        for (Object param : params) {
            pst.setObject(i++, param);
        }

        ResultSet rs = pst.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }

        rs.close();
        pst.close();
        con.close();

        return count;
    }





    }




