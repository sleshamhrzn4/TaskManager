package com.taskmanager.service;

import com.taskmanager.dao.OrganizationDAO;
import com.taskmanager.model.OrganizationModel;

public class OrganizationService {

    private final OrganizationDAO organizationDAO = new OrganizationDAO();

    public OrganizationModel getOrganizationById(long organizationId) throws Exception {
        return organizationDAO.findOrganizationById(organizationId);
    }

    public void addUserToOrganization(long organizationId, long userId, String role) throws Exception {
        organizationDAO.insertOrganizationUser(organizationId, userId, role);
    }
}