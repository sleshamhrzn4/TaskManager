package com.taskmanager.model;

import java.sql.Timestamp;

public class WorkspaceModel {

    private long workspaceId;
    private long organizationId;
    private String workspaceName;
    private Timestamp createdDate;

            public WorkspaceModel(long workspaceId, long organizationId, String workspaceName, Timestamp createdDate){
                this.workspaceId = workspaceId;
                this.organizationId = organizationId;
                this.workspaceName = workspaceName;
                this.createdDate = createdDate;

            }

    public WorkspaceModel() {

    }


    public long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}

