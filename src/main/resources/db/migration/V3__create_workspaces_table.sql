CREATE TABLE workspaces (
                            workspaceId BIGINT NOT NULL AUTO_INCREMENT,
                            organizationId BIGINT NOT NULL,
                            workspaceName VARCHAR(255) NOT NULL,
                            createdDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (workspaceId),
                            CONSTRAINT fk_workspaces_organization FOREIGN KEY (organizationId)
                                REFERENCES organizations (organizationId) ON DELETE CASCADE
);