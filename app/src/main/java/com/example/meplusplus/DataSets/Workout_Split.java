package com.example.meplusplus.DataSets;

public class Workout_Split {
    String split_name;
    String muscles_worked;

    public Workout_Split() {
        this.split_name = "N/A";
        this.muscles_worked = "N/A";
    }

    public Workout_Split(String split_name, String muscles_worked) {
        this.split_name = split_name;
        this.muscles_worked = muscles_worked;
    }

    public String getSplit_name() {
        return split_name;
    }

    public void setSplit_name(String split_name) {
        this.split_name = split_name;
    }

    public String getMuscles_worked() {
        return muscles_worked;
    }

    public void setMuscles_worked(String muscles_worked) {
        this.muscles_worked = muscles_worked;
    }
}
