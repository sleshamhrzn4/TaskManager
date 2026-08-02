CREATE TABLE organizations (
                               organizationId BIGINT NOT NULL AUTO_INCREMENT,
                               organizationName VARCHAR(255) NOT NULL,
                               subscriptionPlan VARCHAR(50) DEFAULT 'free',
                               createdDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (organizationId)
);