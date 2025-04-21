package com.example.meplusplus.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Exercise {
    String exercise_name;
    String muscles_worked;
    String exercise_link;
    String mainSplit;

    public Exercise() {
        this.exercise_name = "N/A";
        this.muscles_worked = "N/A";
        this.exercise_link = "N/A";
        this.mainSplit = "N/A";
    }
}
