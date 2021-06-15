package com.example.todoapp.Model;

public class ToDoModel {
    private int id;
    private String task;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
