package com.example.mytrainingschedules.activities.schedules;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;

import java.util.ArrayList;

/**
 * RecyclerViewAdapter used for: EditScheduleActivity
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class EditScheduleRecyclerViewAdapter extends RecyclerView.Adapter<EditScheduleRecyclerViewAdapter.CustomViewHolder> {

    private ArrayList<Exercise> exerciseList;
    private static RecyclerViewClickListener itemListener;

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView exerciseTitle, exerciseCategory, reps, sets, weight, rest;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseTitle = itemView.findViewById(R.id.activityTitle);
            exerciseCategory = itemView.findViewById(R.id.exerciseCategory);
            reps = itemView.findViewById(R.id.repsTextView);
            sets = itemView.findViewById(R.id.index);
            weight = itemView.findViewById(R.id.weightTextView);
            rest = itemView.findViewById(R.id.rest);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, getLayoutPosition());
        }

    }

    public EditScheduleRecyclerViewAdapter(ArrayList<Exercise> exerciseList, RecyclerViewClickListener itemListener){
        this.exerciseList = exerciseList;
        EditScheduleRecyclerViewAdapter.itemListener = itemListener;
    }

    /* compulsory override methods */
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Exercise currentExercise = exerciseList.get(position);
        holder.exerciseTitle.setText(currentExercise.getName());
        if(currentExercise.getMinReps() == currentExercise.getMaxReps()){
            holder.reps.setText("Reps: " + currentExercise.getMinReps());
        }
        else{
            holder.reps.setText("Reps: " + currentExercise.getMinReps() + " - " + currentExercise.getMaxReps());
        }
        holder.sets.setText("Sets: " + currentExercise.getSetsNumber());
        if(currentExercise.requireEquipment()){
            if(currentExercise.getMinWeight() == currentExercise.getMaxWeight()){
                holder.weight.setText("Weight: " + currentExercise.getMaxWeight() + " kg");
            }
            else{
                holder.weight.setText("Weight: " + currentExercise.getMinWeight() + " - " + currentExercise.getMaxWeight() + " kg");
            }
        }
        else{
            if(currentExercise.getMinWeight() == 0 && currentExercise.getMaxWeight() == 0){
                holder.weight.setText("Weight: bodyweight");
            }
        }
        holder.rest.setText("Rest: " + currentExercise.getRest() + "s");
        holder.exerciseCategory.setText(currentExercise.getCategory());
        switch(currentExercise.getCategory()){
            case "legs":
            case "LEGS":
                holder.exerciseCategory.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.legs_tag));
                break;
            case "arms":
            case "ARMS":
                holder.exerciseCategory.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.arms_tag));
                break;
            case "back":
            case "BACK":
                holder.exerciseCategory.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.back_tag));
                break;
            case "chest":
            case "CHEST":
                holder.exerciseCategory.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.chest_tag));
                break;
            default:
                holder.exerciseCategory.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.default_tag));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

}
