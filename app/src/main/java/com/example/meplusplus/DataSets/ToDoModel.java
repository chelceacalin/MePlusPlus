package com.example.meplusplus.DataSets;

import androidx.annotation.NonNull;

public class ToDoModel {
    String taskID;
    String description;
    String task;
    String date;

    public ToDoModel() {
        this.taskID = "N/A";
        this.description = "N/A";
        this.task = "N/A";
        this.date = "N/A";

    }

    public ToDoModel(String taskID, String description, String task, String date) {
        this.taskID = taskID;
        this.description = description;
        this.task = task;
        this.date = date;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return "ToDoModel{" +
                "taskID='" + taskID + '\'' +
                ", description='" + description + '\'' +
                ", task='" + task + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
