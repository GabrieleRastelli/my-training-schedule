package com.pomegranatesoftware.mytrainingschedules.activities;
import java.io.Serializable;

/**
 * The Set class is used to represent reps and weight in an Exercise.
 *
 * @author Mattia Gualtieri
 * @author Gabriele Rastelli
 */

public class Set implements Serializable {

    private int reps;
    private int weight;
    private boolean requireEquipment;

    /**
     * Constructs a Set class.
     * The attribute requireEquipment is automatically calculated based on
     * the weight parameter.
     * @param reps
     * @param weight
     */
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

    /**
     * reps getter method
     * @return reps of the Set
     */
    public int getReps() {
        return reps;
    }

    /**
     * reps setter method
     * @param reps
     */
    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     * weight getter method
     * @return weight of the Set
     */
    public int getWeight() {
        return weight;
    }

    /**
     * weight setter method
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * requireEquipment getter method
     * @return true if the Set requires equipment, false otherwise
     */
    public boolean requireEquipment(){
        return this.requireEquipment;
    }

    /**
     * requireEquipment setter method
     * @param requireEquipment
     */
    public void setRequireEquipment(boolean requireEquipment){
        this.requireEquipment = requireEquipment;
    }

}
