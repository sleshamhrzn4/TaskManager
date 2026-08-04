CREATE TABLE users (
                       userId INT NOT NULL AUTO_INCREMENT,
                       userName VARCHAR(50) NOT NULL,
                       userEmail VARCHAR(250) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) DEFAULT 'user',
                       PRIMARY KEY (userId),
                       UNIQUE KEY uq_users_email (userEmail)
);