CREATE TABLE organizations (
                               organizationId BIGINT AUTO_INCREMENT PRIMARY KEY,
                               organizationName VARCHAR(255) NOT NULL,
                               subscriptionPlan VARCHAR(50) DEFAULT 'free',
                               createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;