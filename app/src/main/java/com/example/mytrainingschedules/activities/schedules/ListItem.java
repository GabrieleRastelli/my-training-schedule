package com.example.mytrainingschedules.activities.schedules;

import org.json.JSONException;
import org.json.JSONObject;

public class ListItem {

    private String title;
    private String type;
    private int sets;
    private int reps;
    private int weight;
    private String equipment;
    private int rest;

    public ListItem(String title, String type, int sets, int reps, int weight, String equipment, int rest) {
        this.title = title;
        this.type = type;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.equipment = equipment;
        this.rest = rest;
    }

    public ListItem(JSONObject exercise) {
        try {
            this.title = exercise.getString("exercise-name");
            this.type = exercise.getString("type");
            this.sets = exercise.getInt("sets");
            this.reps = exercise.getInt("reps");
            this.weight = exercise.getInt("weight");;
            this.equipment = exercise.getString("equipment");
            this.rest = exercise.getInt("rest-between-sets");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }
}
