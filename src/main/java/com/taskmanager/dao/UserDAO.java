package com.taskmanager.dao;

import com.taskmanager.model.UserModel;
import com.taskmanager.utils.DBConfig;

import java.sql.*;


public class UserDAO {

    public void insertUser(String userName, String userEmail, String password, String role, long organizationId) throws Exception {
        String insertUserSql = "INSERT INTO users (userName, userEmail, password, role) VALUES (?,?,?,?)";
        String insertOrgUserSql = "INSERT INTO organization_users (organizationId, userId, role) VALUES (?,?,?)";

        Connection con = null;
        try {
            con = DBConfig.getConnection();
            con.setAutoCommit(false); // start transaction — both inserts succeed together, or neither does

            int newUserId;
            try (PreparedStatement pst = con.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, userName);
                pst.setString(2, userEmail);
                pst.setString(3, password);
                pst.setString(4, role != null ? role : "user");
                pst.executeUpdate();

                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        newUserId = rs.getInt(1);
                        System.out.println("New user inserted with ID:" + newUserId);
                    } else {
                        throw new SQLException("Failed to retrieve generated userId");
                    }
                }
            }

            try (PreparedStatement pst = con.prepareStatement(insertOrgUserSql)) {
                pst.setLong(1, organizationId);
                pst.setInt(2, newUserId);
                pst.setString(3, "member");
                pst.executeUpdate();
            }

            con.commit();

        } catch (Exception e) {
            if (con != null) {
                con.rollback();
            }
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }


    public UserModel getUserByEmail(String email)
        throws Exception{
        UserModel user= null;
        String sql = "SELECT u.userId, u.userName, u.userEmail, u.password, u.role, w.workspaceId " +
                "FROM users u " +
                "JOIN organization_users ou ON u.userId = ou.userId " +
                "JOIN workspaces w ON w.organizationId = ou.organizationId " +
                "WHERE u.userEmail = ? " +
                "ORDER BY w.workspaceId ASC " +
                "LIMIT 1";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, email);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    user = new UserModel();
                    user.setUserId(rs.getInt("userId"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserEmail(rs.getString("userEmail"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setWorkspaceId(rs.getInt("workspaceId"));
                }
            }
        }

        return user;

    }
}
