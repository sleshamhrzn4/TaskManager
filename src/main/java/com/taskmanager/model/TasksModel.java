package com.taskmanager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TasksModel {
    private int taskId;
    private int userId;
    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime createdDate;
    private LocalDate dueDate;


    public TasksModel(int taskId, int userId, String title, String description, String priority, String status, LocalDateTime createdDate, LocalDate dueDate){
        this.taskId= taskId;
        this.userId = userId;
        this.title= title;
        this.description= description;
        this.priority= priority;
        this.status= status;
        this.createdDate= createdDate;
        this.dueDate= dueDate;


    }

    public int getTaskId() {
        return taskId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }


    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
