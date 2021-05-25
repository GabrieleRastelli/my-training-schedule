package com.example.mytrainingschedules.activities;

import java.io.Serializable;

public class Set implements Serializable {

    private int reps;
    private int weight;
    private boolean requireEquipment;

    public Set(int reps, int weight){
        this.reps = reps;
        this.weight = weight;
        if(this.weight != 0){
            this.requireEquipment = true;
        }
        else{
            this.requireEquipment = false;
        }
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

    public boolean requireEquipment(){
        return this.requireEquipment;
    }

    public void setRequireEquipment(boolean requireEquipment){
        this.requireEquipment = requireEquipment;
    }

}
