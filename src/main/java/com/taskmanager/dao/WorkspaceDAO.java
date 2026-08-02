package com.taskmanager.dao;

import com.taskmanager.model.WorkspaceModel;
import com.taskmanager.utils.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceDAO {

    public void insertWorkspace(Connection con, WorkspaceModel workspace) throws Exception {
        String sql = "INSERT INTO workspaces(organizationId,workspaceName) VALUES (?,?)";

        try (PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setLong(1, workspace.getOrganizationId());
            pst.setString(2, workspace.getWorkspaceName());

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    rs.getLong(1);
                } else {
                    throw new Exception("Workspace insert failed, no ID obtained");
                }
            }
        }
    }

    public List<WorkspaceModel> getWorkspacesByOrganizationId(long organizationId) throws Exception {
        String sql = "SELECT workspaceId, organizationId, workspaceName, createdDate FROM workspaces WHERE organizationId = ?";
        List<WorkspaceModel> workspaces = new ArrayList<>();

        try (Connection con = DBConfig.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setLong(1, organizationId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    WorkspaceModel workspace = new WorkspaceModel();
                    workspace.setWorkspaceId(rs.getLong("workspaceId"));
                    workspace.setOrganizationId(rs.getLong("organizationId"));
                    workspace.setWorkspaceName(rs.getString("workspaceName"));
                    workspace.setCreatedDate(rs.getTimestamp("createdDate"));
                    workspaces.add(workspace);
                }
            }
        }

        return workspaces;
    }
}