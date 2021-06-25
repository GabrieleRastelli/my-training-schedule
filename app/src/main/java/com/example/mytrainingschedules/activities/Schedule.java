package com.example.mytrainingschedules.activities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Schedule class is composed by a list of Exercise, and other attributes.
 * @author Mattia Gualtieri
 * @author Gabriele Rastelli
 */

public class Schedule implements Serializable {

    private String title;
    private String description;
    private boolean requireEquipment;
    ArrayList<Exercise> exercises;

    /**
     * Empty constructor of the class
     */
    public Schedule(){
        this.title = "";
        this.description = "";
        this.exercises = new ArrayList<Exercise>();
        this.requireEquipment = false;
    }

    /**
     * Constructor of the class
     * @param title
     * @param description
     * @param exercises
     */
    public Schedule(String title, String description, ArrayList<Exercise> exercises) {
        this.title = title;
        this.description = description;
        this.exercises = exercises;
        this.requireEquipment = false;
        for (Exercise exercise: exercises) {
            if(exercise.requireEquipment()){
                this.requireEquipment = true;
            }
        }
    }

    /**
     * Constructor of the class that parse a JSONArray into the list of exercises
     * @param title
     * @param description
     * @param exercises
     * @throws JSONException
     */
    public Schedule(String title, String description, JSONArray exercises) throws JSONException {
        this.title = title;
        this.description = description;
        this.requireEquipment = false;
        this.exercises = new ArrayList<Exercise>();
        for(int i = 0; i < exercises.length(); i++){
            Exercise exercise = new Exercise(exercises.getJSONObject(i));
            this.exercises.add(exercise);
            if(exercise.requireEquipment()){
                this.requireEquipment = true;
            }
        }
    }

    /**
     * title getter method
     * @return title of the Schedule
     */
    public String getTitle() {
        return title;
    }

    /**
     * title setter method
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * description getter method
     * @return description of the Schedule
     */
    public String getDescription() {
        return description;
    }

    /**
     * description setter method
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * requireEquipment getter method
     * @return true if the Schedule requires equipment, false otherwise
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
     * exercises getter method
     * @return an ArrayList of exercises
     */
    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    /**
     * exercises setter method
     * @param exercises
     */
    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    /**
     * Number of exercises in the Schedule
     * @return number of exercises in the Schedule
     */
    public int lenght(){
        return this.exercises.size();
    }

    /**
     * Add an exercise to the Schedule
     * @param exercise
     */
    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    /**
     * Parse the exercises ArrayList into a JSONObject
     * @return a JSONObject, representing the list of exercises in the Schedule
     * @throws JSONException
     */
    public JSONObject getJsonExercises() throws JSONException {
        JSONArray exercises = new JSONArray();
        for (Exercise currentExercise: this.exercises) {
            exercises.put(currentExercise.getJsonExercise());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("exercises", exercises);
        return jsonObject;
    }

}
