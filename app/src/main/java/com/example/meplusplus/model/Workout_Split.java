package com.example.meplusplus.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Workout_Split {
    String split_name;
    String muscles_worked;

    public Workout_Split() {
        this.split_name = "N/A";
        this.muscles_worked = "N/A";
    }
}
