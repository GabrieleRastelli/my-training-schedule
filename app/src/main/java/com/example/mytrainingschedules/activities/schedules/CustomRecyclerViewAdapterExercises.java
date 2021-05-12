package com.example.mytrainingschedules.activities.schedules;

import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;

import java.util.ArrayList;
import java.util.List;

public class CustomRecyclerViewAdapterExercises extends RecyclerView.Adapter<CustomRecyclerViewAdapterExercises.CustomViewHolder> implements Filterable {

    private ArrayList<Exercise> exerciseList;
    private ArrayList<Exercise> exerciseListFull;
    private static RecyclerViewClickListener itemListener;
    private String selectedTitle = null;
    private String selectedCategory = null;


    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Exercise> filteredExercises = new ArrayList<Exercise>();
            if(constraint == null || constraint.length() == 0){
                filteredExercises.addAll(exerciseListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Exercise exercise: exerciseListFull){
                    if(exercise.getName().toLowerCase().contains(filterPattern)){
                        filteredExercises.add(exercise);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredExercises;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exerciseList.clear();
            exerciseList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView exerciseTitle, exerciseCategory, checkIcon;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseTitle = itemView.findViewById(R.id.exerciseTitle);
            exerciseCategory = itemView.findViewById(R.id.exerciseCategory);
            checkIcon = itemView.findViewById(R.id.checkIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, getLayoutPosition());
        }
    }

    public CustomRecyclerViewAdapterExercises(ArrayList<Exercise> exerciseList, RecyclerViewClickListener itemListener){
        this.exerciseList = exerciseList;
        this.exerciseListFull = new ArrayList<Exercise>(exerciseList);
        CustomRecyclerViewAdapterExercises.itemListener = itemListener;
    }

    public void setData(ArrayList<Exercise> exerciseList){
        this.exerciseList = exerciseList;
        this.exerciseListFull = new ArrayList<Exercise>(exerciseList);
        notifyDataSetChanged();
    }

    /* compulsory override methods */
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_exercise_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Exercise currentExercise = exerciseList.get(position);
        holder.exerciseTitle.setText(currentExercise.getName());
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
        if(holder.exerciseTitle.getText().toString().equals(selectedTitle) && holder.exerciseCategory.getText().equals(selectedCategory)){
            holder.checkIcon.setVisibility(View.VISIBLE);
        }
        else {
            holder.checkIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void setSelectedTitle(String title){
        selectedTitle = title;
    }

    public void setSelectedCategory(String category){
        selectedCategory = category;
    }

}
