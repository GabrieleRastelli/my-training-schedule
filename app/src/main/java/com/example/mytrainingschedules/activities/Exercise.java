package com.example.mytrainingschedules.activities;

public class Exercise {

    private String name;
    private int sets;
    private int reps;
    private float weight;
    private String category;
    private boolean requireEquipment;

    public Exercise(String name, int sets, int reps, float weight, String category, boolean requireEquipment) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.category = category;
        this.requireEquipment = requireEquipment;
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
