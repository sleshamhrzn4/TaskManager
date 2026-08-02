package com.taskmanager.model;
import java.sql.Timestamp;

public class OrganizationModel {

    private long organizationId;
    private String organizationName;
    private String subscriptionPlan;
    private Timestamp createdDate;


    public OrganizationModel() {
    }

    public OrganizationModel(String organizationName, String subscriptionPlan) {

        this.organizationName = organizationName;
        this.subscriptionPlan = subscriptionPlan;

    }


    public OrganizationModel(long organizationId,String organizationName, String subscriptionPlan, Timestamp createdDate) {
        this.organizationId= organizationId;
        this.organizationName = organizationName;
        this.subscriptionPlan = subscriptionPlan;
        this.createdDate=createdDate;
    }




    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(String subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}


