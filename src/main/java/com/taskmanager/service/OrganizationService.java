package com.taskmanager.service;

import com.taskmanager.dao.OrganizationDAO;
import com.taskmanager.dao.WorkspaceDAO;
import com.taskmanager.model.OrganizationModel;
import com.taskmanager.model.WorkspaceModel;
import com.taskmanager.utils.DBConfig;

import java.sql.Connection;

public class OrganizationService {

    private final OrganizationDAO organizationDAO = new OrganizationDAO();
    private final WorkspaceDAO workspaceDAO = new WorkspaceDAO();

    public long createOrganization(OrganizationModel organization) throws Exception {

        if (organizationDAO.existsByName(organization.getOrganizationName())) {
            throw new Exception("An organization named '" + organization.getOrganizationName() + "' already exists.");
        }

        Connection con = null;
        try {
            con = DBConfig.getConnection();
            con.setAutoCommit(false);

            long newOrganizationId = organizationDAO.insertOrganization(con, organization);
            System.out.println("New org ID: " + newOrganizationId);

            WorkspaceModel defaultWorkspace = new WorkspaceModel();
            defaultWorkspace.setOrganizationId(newOrganizationId);
            defaultWorkspace.setWorkspaceName("General");
            long newWorkspaceId = workspaceDAO.insertWorkspace(con, defaultWorkspace);
            System.out.println("New default workspace ID: " + newWorkspaceId);

            con.commit();
            return newOrganizationId;

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

    public OrganizationModel getOrganizationById(long organizationId) throws Exception {
        return organizationDAO.findOrganizationById(organizationId);
    }

    public void addUserToOrganization(long organizationId, long userId, String role) throws Exception {
        organizationDAO.insertOrganizationUser(organizationId, userId, role);
    }
}