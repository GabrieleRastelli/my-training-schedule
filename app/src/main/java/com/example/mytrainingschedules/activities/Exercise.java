package com.example.mytrainingschedules.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Exercise implements Serializable {

    private String name;
    private ArrayList<Set> sets;
    private int rest_between_sets;
    private int rest_between_exercises;
    private String category;
    private boolean requireEquipment;

    public Exercise(String name, ArrayList<Set> sets, int rest_between_sets, int rest_between_exercises, String category) {
        this.name = name;
        this.sets = sets;
        this.rest_between_sets = rest_between_sets;
        this.rest_between_exercises = rest_between_exercises;
        this.category = category;
        for(Set set: this.sets) {
            if(set.requireEquipment() == true){
                this.requireEquipment = true;
                return;
            }
        }
        this.requireEquipment = false;
    }

    public Exercise(String title, String category, boolean requireEquipment){
        this.name = title;
        this.sets = null;
        this.rest_between_sets = -1;
        this.rest_between_exercises = -1;
        this.category = category;
        this.requireEquipment = requireEquipment;
    }

    public Exercise(JSONObject exercise) throws JSONException {
        this.name = exercise.getString("exercise-name");
        this.requireEquipment = false;
        this.sets = new ArrayList<Set>();
        JSONArray setsJsonArray = exercise.getJSONArray("sets");
        for(int i = 0; i < setsJsonArray.length(); i++) {
            JSONObject setJsonObject = setsJsonArray.getJSONObject(i);
            Set set = new Set(setJsonObject.getInt("reps"), setJsonObject.getInt("weight"));
            if(set.getWeight() == 0){
                set.setRequireEquipment(false);
            }
            else{
                set.setRequireEquipment(true);
                this.requireEquipment = true;
            }
            this.sets.add(set);
        }
        this.rest_between_sets = exercise.getInt("rest-between-sets");
        this.rest_between_exercises = exercise.getInt("rest-between-exercises");
        // TODO: change response from "type" to "category"
        this.category = exercise.getString("type");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Set> getSets() {
        return sets;
    }

    public void setSets(ArrayList<Set> sets) {
        this.sets = sets;
        this.requireEquipment = false;
        for(Set set: sets){
            if(set.requireEquipment()){
                this.requireEquipment = true;
            }
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean requireEquipment() {
        return requireEquipment;
    }

    public void setRequireEquipment(boolean requireEquipment) {
        this.requireEquipment = requireEquipment;
    }

    public int getRest() {
        return rest_between_sets;
    }

    public int getRest_between_exercises() {
        return rest_between_exercises;
    }

    public void setRest(int rest_between_sets) {
        this.rest_between_sets = rest_between_sets;
    }

    public void setRest_between_exercises(int rest_between_exercises) {
        this.rest_between_exercises = rest_between_exercises;
    }

    public JSONObject getJsonExercise() throws JSONException {
        JSONObject exercise = new JSONObject();
        exercise.put("exercise-name", this.name);
        JSONArray sets = new JSONArray();
        for(Set set: this.sets) {
           JSONObject setJsonObject = new JSONObject();
           setJsonObject.put("reps", set.getReps());
           setJsonObject.put("weight", set.getWeight());
           sets.put(setJsonObject);
        }
        exercise.put("sets", sets);
        exercise.put("rest-between-sets", rest_between_sets);
        exercise.put("rest-between-exercises", rest_between_exercises);
        exercise.put("type", category);
        if(requireEquipment){
            exercise.put("equipment", "TRUE");
        }
        else{
            exercise.put("equipment", "FALSE");
        }

        return exercise;
    }

    public int getSetsNumber(){
        return this.sets.size();
    }

    public int getMinReps(){
        if(this.sets.size() == 0){
            return 0;
        }
        int minReps = this.sets.get(0).getReps();
        for(Set set: this.sets){
            if(set.getReps() < minReps){
                minReps = set.getReps();
            }
        }
        return minReps;
    }

    public int getMaxReps(){
        if(this.sets.size() == 0){
            return 0;
        }
        int maxReps = this.sets.get(0).getReps();
        for(Set set: this.sets){
            if(set.getReps() > maxReps){
                maxReps = set.getReps();
            }
        }
        return maxReps;
    }

    public int getMinWeight(){
        if(this.sets.size() == 0){
            return 0;
        }
        int minWeight = this.sets.get(0).getWeight();
        for(Set set: this.sets){
            if(set.getWeight() < minWeight){
                minWeight = set.getWeight();
            }
        }
        return minWeight;
    }

    public int getMaxWeight(){
        if(this.sets.size() == 0){
            return 0;
        }
        int maxWeight = this.sets.get(0).getWeight();
        for(Set set: this.sets){
            if(set.getWeight() > maxWeight){
                maxWeight = set.getWeight();
            }
        }
        return maxWeight;
    }
}
