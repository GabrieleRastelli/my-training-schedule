package com.pomegranatesoftware.mytrainingschedules.activities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Exercise class is composed by a list of Set, and other attributes.
 *
 * @author Mattia Gualtieri
 * @author Gabriele Rastelli
 */

public class Exercise implements Serializable {

    private String name;
    private ArrayList<Set> sets;
    private int rest_between_sets;
    private int rest_between_exercises;
    private String category;
    private boolean requireEquipment;

    /**
     * Basic constructor of the Exercise class.
     * All class attributes are required as parameters, but requireEquipment
     * attribute that is automatically calculated.
     * @param name
     * @param sets
     * @param rest_between_sets
     * @param rest_between_exercises
     * @param category
     */
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

    /**
     * "Preview" constructor of the Exercise class.
     * Is used to create a preview of the exercise, with just his basic information
     * (title, category and requireEquipment)
     * @param title
     * @param category
     * @param requireEquipment
     */
    public Exercise(String title, String category, boolean requireEquipment){
        this.name = title;
        this.sets = null;
        this.rest_between_sets = -1;
        this.rest_between_exercises = -1;
        this.category = category;
        this.requireEquipment = requireEquipment;
    }

    /**
     * Constructor of the class that parse a JSONObject to an Exercise object.
     * @param exercise
     * @throws JSONException
     */
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

    /**
     * name getter method
     * @return the name of the Exercise
     */
    public String getName() {
        return name;
    }

    /**
     * name setter method
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets getter method
     * @return The ArrayList of Sets
     */
    public ArrayList<Set> getSets() {
        return sets;
    }

    /**
     * sets setter method
     * @param sets
     */
    public void setSets(ArrayList<Set> sets) {
        this.sets = sets;
        this.requireEquipment = false;
        for(Set set: sets){
            if(set.requireEquipment()){
                this.requireEquipment = true;
            }
        }
    }

    /**
     * category getter method
     * @return the category of the Exercise
     */
    public String getCategory() {
        return category;
    }

    /**
     * category setter method
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * requireEquipment getter method
     * @return true if the Exercise requires equipment, false otherwise
     */
    public boolean requireEquipment() {
        return requireEquipment;
    }

    /**
     * requireEquipment setter method
     * @param requireEquipment
     */
    public void setRequireEquipment(boolean requireEquipment) {
        this.requireEquipment = requireEquipment;
    }

    /**
     * rest getter method
     * @return rest between sets
     */
    public int getRest() {
        return rest_between_sets;
    }

    /**
     * rest setter method
     * @param rest_between_sets
     */
    public void setRest(int rest_between_sets) {
        this.rest_between_sets = rest_between_sets;
    }

    /**
     * rest_between_exercises getter method
     * @return the rest between exercises
     */
    public int getRest_between_exercises() {
        return rest_between_exercises;
    }

    /**
     * rest_between_exercises setter method
     * @param rest_between_exercises
     */
    public void setRest_between_exercises(int rest_between_exercises) {
        this.rest_between_exercises = rest_between_exercises;
    }

    /**
     * parse the Exercise to a JSONObject
     * @return a JSONObject, representing the Exercise
     * @throws JSONException
     */
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

    /**
     * get the number of sets
     * @return the number of sets
     */
    public int getSetsNumber(){
        return this.sets.size();
    }

    /**
     * get the minimum value of reps in the sets
     * @return the minimum value of reps in the sets
     */
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

    /**
     * get the maximum value of reps in the sets
     * @return the maximum value of reps in the sets
     */
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

    /**
     * get the minimum value of weight in the sets
     * @return the minimum value of weight in the sets
     */
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

    /**
     * get the maximum value of reps in the sets
     * @return the maximum value of reps in the sets
     */
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
