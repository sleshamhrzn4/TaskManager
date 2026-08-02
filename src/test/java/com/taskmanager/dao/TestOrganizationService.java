package com.taskmanager.dao;

import com.taskmanager.model.OrganizationModel;
import com.taskmanager.service.OrganizationService;

public class TestOrganizationService {
    public static void main(String[] args) {
        try {
            OrganizationService organizationService = new OrganizationService();

            OrganizationModel org = new OrganizationModel();
            org.setOrganizationName("AlfaBeta");
            org.setSubscriptionPlan("free");

            long newOrgId = organizationService.createOrganization(org);
            System.out.println("Created organization with ID: " + newOrgId);

        } catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }
}