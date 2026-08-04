package com.taskmanager.service;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.TaskModel;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.UnauthorizedAccessException;
import com.taskmanager.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TaskService {

    private final TaskDAO taskDAO = new TaskDAO();

    public void createTask(int userId, int workspaceId, String title, String description,
                           String priority, LocalDate dueDate) throws Exception {

        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Task title is required.");
        }

        TaskModel task = new TaskModel();
        task.setUserId(userId);
        task.setWorkspaceId(workspaceId);
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus("todo");
        task.setCreatedDate(LocalDateTime.now());
        task.setDueDate(dueDate);

        taskDAO.addTask(task);
    }

    public List<TaskModel> getTasksForWorkspace(int userId, int workspaceId, String search,
                                                String priorityFilter, String statusFilter,
                                                boolean overdueOnly, String sortBy, String sortDir,
                                                int pageNumber, int pageSize) throws Exception {

        return taskDAO.getTasksPaged(userId, workspaceId, search, priorityFilter, statusFilter,
                overdueOnly, sortBy, sortDir, pageNumber, pageSize);
    }

    public int getTotalTaskCount(int userId, int workspaceId, String search, String priorityFilter,
                                 String statusFilter, boolean overdueOnly) throws Exception {

        return taskDAO.getTotalTaskCount(userId, workspaceId, search, priorityFilter, statusFilter, overdueOnly);
    }

    public void updateTask(int taskId, int workspaceId, String title, String description,
                           String priority, String status, LocalDate dueDate) throws Exception {

        TaskModel task = getOwnedTask(taskId, workspaceId);

        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);
        task.setDueDate(dueDate);

        taskDAO.updateTask(task);
    }

    public void deleteTask(int taskId, int workspaceId) throws Exception {
        getOwnedTask(taskId, workspaceId);
        taskDAO.deleteTask(taskId);
    }

    private TaskModel getOwnedTask(int taskId, int workspaceId) throws Exception {
        TaskModel task = taskDAO.getTaskById(taskId);
        if (task == null) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
        if (task.getWorkspaceId() != workspaceId) {
            throw new UnauthorizedAccessException("You do not have access to this task.");
        }
        return task;
    }
}