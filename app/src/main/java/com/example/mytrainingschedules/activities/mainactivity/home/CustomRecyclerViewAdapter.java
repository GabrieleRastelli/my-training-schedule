package com.example.mytrainingschedules.activities.mainactivity.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;

import java.util.ArrayList;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

    private ArrayList<Exercise> exerciseList;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView exerciseTitle, exerciseCategory, reps, sets, weight, rest;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseTitle = itemView.findViewById(R.id.exerciseTitle);
            exerciseCategory = itemView.findViewById(R.id.exerciseCategory);
            reps = itemView.findViewById(R.id.reps);
            sets = itemView.findViewById(R.id.sets);
            weight = itemView.findViewById(R.id.weight);
            rest = itemView.findViewById(R.id.rest);
        }

    }

    public CustomRecyclerViewAdapter(ArrayList<Exercise> exerciseList){
        this.exerciseList = exerciseList;
    }

    /* compulsory override methods */
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_exercise_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Exercise currentExercise = exerciseList.get(position);
        holder.exerciseTitle.setText(currentExercise.getName());
        holder.reps.setText("Reps: " + currentExercise.getReps());
        holder.sets.setText("Sets: " + currentExercise.getSets());
        if(currentExercise.isRequireEquipment()){
            holder.weight.setText("Weight: " + currentExercise.getWeight() + " kg");
        }
        else{
            holder.weight.setText("Weight: bodyweight");
        }
        holder.rest.setText("Rest: " + currentExercise.getRest_between_sets() + "s");
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
