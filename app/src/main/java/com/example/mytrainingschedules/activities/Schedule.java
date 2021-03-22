package com.example.mytrainingschedules.activities;

import java.util.ArrayList;

public class Schedule {

    private String title;
    private String description;
    private boolean requireEquipment;
    ArrayList<Exercise> exercises;

    public Schedule(String title, String description, ArrayList<Exercise> exercises) {
        this.title = title;
        this.description = description;
        this.exercises = exercises;
        this.requireEquipment = false;
        for (Exercise exercise: exercises) {
            if(exercise.isRequireEquipment()){
                this.requireEquipment = true;
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequireEquipment() {
        return requireEquipment;
    }

    public void setRequireEquipment(boolean requireEquipment) {
        this.requireEquipment = requireEquipment;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}
