package com.example.meplusplus.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
}
