package com.taskmanager.dao;

import com.taskmanager.model.OrganizationModel;
import com.taskmanager.utils.DBConfig;

import java.sql.*;

public class OrganizationDAO {

    public long insertOrganization(Connection con, OrganizationModel organization) throws Exception {
        String sql = "INSERT INTO organizations (organizationName, subscriptionPlan) VALUES (?, ?)";

        try (PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, organization.getOrganizationName());
            pst.setString(2, organization.getSubscriptionPlan());

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new Exception("Organization insert failed, no ID obtained.");
                }
            }
        }
}

    public OrganizationModel findOrganizationById(long organizationId) throws Exception{
        String sql= "SELECT organizationId,organozationName, subscriptionPlan,createdDate FROM organizations WHERE organizationId=?";

        try (Connection con= DBConfig.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)){

            pst.setLong(1,organizationId);

            try(ResultSet rs = pst.executeQuery()){
                if (rs.next()){
                    OrganizationModel org = new OrganizationModel();
                    org.setOrganizationId(rs.getLong("organizationId"));
                    org.setOrganizationName(rs.getString("organizationName"));
                    org.setSubscriptionPlan(rs.getString("subscriptionPlan"));
                    org.setCreatedDate(rs.getTimestamp("createdDate"));
                    return org;
                }else{
                    return null;
                }
            }

        }
    }

    public void insertOrganizationUser(long organizationId, long userId, String role) throws Exception {
        String sql = "INSERT INTO organization_users (organizationId, userId, role) VALUES (?, ?, ?)";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setLong(1, organizationId);
            pst.setLong(2, userId);
            pst.setString(3, role);


            pst.executeUpdate();
        }
    }


    public boolean existsByName(String organizationName) throws Exception {
        String sql = "SELECT COUNT(*) FROM organizations WHERE organizationName = ?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, organizationName);

            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }



}
