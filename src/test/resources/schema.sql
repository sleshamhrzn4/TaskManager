CREATE TABLE tasks (
                       taskId INT AUTO_INCREMENT PRIMARY KEY,
                       userId INT NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(1000),
                       priority VARCHAR(20),
                       status VARCHAR(20),
                       createdDate TIMESTAMP NOT NULL,
                       dueDate DATE
);