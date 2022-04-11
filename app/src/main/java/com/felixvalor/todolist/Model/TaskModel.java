package com.felixvalor.todolist.Model;

public class TaskModel {

    private int idTask,status,id_user;
    private String description, title;

    public TaskModel(int id_task, int id_user, String description, int status, String title) {
        this.idTask = id_task;
        this.id_user = id_user;
        this.status = status;
        this.description = description;
        this.title = title;
    }

    public TaskModel() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
