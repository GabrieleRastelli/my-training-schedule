package com.example.mytrainingschedules.activities;

import org.json.JSONException;
import org.json.JSONObject;

public class Exercise {

    private String name;
    private int sets;
    private int reps;
    private float weight;
    private int rest_between_sets;
    private int rest_between_exercises;
    private String category;
    private boolean requireEquipment;

    public Exercise(String name, int sets, int reps, float weight, int rest_between_sets, int rest_between_exercises, String category) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.rest_between_sets = rest_between_sets;
        this.rest_between_exercises = rest_between_exercises;
        this.category = category;
        if(this.weight != 0){
            this.requireEquipment = true;
        }
    }

    public Exercise(String title, String category, boolean requireEquipment){
        this.name = title;
        this.sets = -1;
        this.reps = -1;
        this.weight = -1;
        this.rest_between_sets = -1;
        this.rest_between_exercises = -1;
        this.category = category;
        this.requireEquipment = requireEquipment;
    }

    public Exercise(JSONObject exercise) throws JSONException {
        this.name = exercise.getString("exercise-name");
        //this.id = exercise.getInt("id");
        this.sets = exercise.getInt("sets");
        this.reps = exercise.getInt("reps");
        this.weight = exercise.getInt("weight");
        this.rest_between_sets = exercise.getInt("rest-between-sets");
        this.rest_between_exercises = exercise.getInt("rest-between-exercises");
        this.category = "category";
        if(this.weight != 0){
            this.requireEquipment = true;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isRequireEquipment() {
        return requireEquipment;
    }

    public void setRequireEquipment(boolean requireEquipment) {
        this.requireEquipment = requireEquipment;
    }
}
