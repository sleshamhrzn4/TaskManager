CREATE TABLE organization_users (
                                    organizationId BIGINT NOT NULL,
                                    userId INT NOT NULL,
                                    role VARCHAR(50) NOT NULL DEFAULT 'member',
                                    joinedDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY (organizationId, userId),
                                    CONSTRAINT fk_orgusers_organization FOREIGN KEY (organizationId)
                                        REFERENCES organizations (organizationId) ON DELETE CASCADE,
                                    CONSTRAINT fk_orgusers_user FOREIGN KEY (userId)
                                        REFERENCES users (userId) ON DELETE CASCADE
);