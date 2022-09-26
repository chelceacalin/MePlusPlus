package com.example.meplusplus.DataSets;

public class Exercise {
    String exercise_name;
    String muscles_worked;
    String exercise_link;
    String mainSplit;
    public Exercise() {
        this.exercise_name="N/A";
        this.muscles_worked = "N/A";
        this.exercise_link="N/A";
        this.mainSplit="N/A";
    }

    public Exercise(String exercise_name, String muscles_worked,String exercise_link,String mainSplit) {
        this.exercise_name = exercise_name;
        this.muscles_worked = muscles_worked;
        this.exercise_link=exercise_link;
        this.mainSplit=mainSplit;
    }

    public String getMainSplit() {
        return mainSplit;
    }

    public void setMainSplit(String mainSplit) {
        this.mainSplit = mainSplit;
    }

    public String getExercise_link() {
        return exercise_link;
    }

    public void setExercise_link(String exercise_link) {
        this.exercise_link = exercise_link;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public String getMuscles_worked() {
        return muscles_worked;
    }

    public void setMuscles_worked(String muscles_worked) {
        this.muscles_worked = muscles_worked;
    }


}
