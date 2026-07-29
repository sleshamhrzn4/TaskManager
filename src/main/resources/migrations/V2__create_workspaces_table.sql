CREATE TABLE workspaces (
                            workspaceId BIGINT AUTO_INCREMENT PRIMARY KEY,
                            organizationId BIGINT NOT NULL,
                            workspaceName VARCHAR(255) NOT NULL,
                            createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_workspace_org
                                FOREIGN KEY (organizationId) REFERENCES organizations(organizationId)
                                    ON DELETE CASCADE
) ENGINE=InnoDB;