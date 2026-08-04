CREATE TABLE tasks (
                       taskId INT NOT NULL AUTO_INCREMENT,
                       userId INT,
                       title VARCHAR(250),
                       description TEXT,
                       priority VARCHAR(20) DEFAULT 'Medium',
                       status VARCHAR(20) DEFAULT 'Pending',
                       createdDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       dueDate DATE,
                       workspaceId BIGINT,
                       PRIMARY KEY (taskId),
                       CONSTRAINT fk_tasks_user FOREIGN KEY (userId)
                           REFERENCES users (userId) ON DELETE CASCADE,
                       CONSTRAINT fk_tasks_workspace FOREIGN KEY (workspaceId)
                           REFERENCES workspaces (workspaceId) ON DELETE CASCADE
);