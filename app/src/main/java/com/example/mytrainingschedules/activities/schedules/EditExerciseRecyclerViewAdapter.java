package com.example.mytrainingschedules.activities.schedules;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Set;

import java.util.ArrayList;

/**
 * RecyclerViewAdapter used for: EditExerciseActivity
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class EditExerciseRecyclerViewAdapter extends RecyclerView.Adapter<EditExerciseRecyclerViewAdapter.CustomViewHolder> {

    private ArrayList<Set> sets;
    private static RecyclerViewClickListener itemListener;

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView index;
        public NumberPicker repsPicker, weightPicker;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.index);
            repsPicker = itemView.findViewById(R.id.repsPicker);
            weightPicker = itemView.findViewById(R.id.weightPicker);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, getLayoutPosition());
        }

    }

    public EditExerciseRecyclerViewAdapter(ArrayList<Set> sets, RecyclerViewClickListener itemListener){
        this.sets = sets;
        EditExerciseRecyclerViewAdapter.itemListener = itemListener;
    }

    /* compulsory override methods */
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Set currentSet = sets.get(position);
        holder.index.setText(position + 1 + "");
        String[] reps = new String[100];
        for(int i = 0; i < reps.length; i++){
            reps[i] = Integer.toString(i);
        }
        holder.repsPicker.setDisplayedValues(reps);
        holder.repsPicker.setMinValue(0);
        holder.repsPicker.setMaxValue(reps.length - 1);
        holder.repsPicker.setWrapSelectorWheel(false);
        holder.repsPicker.setValue(currentSet.getReps());
        holder.repsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldV, int newV) {
                currentSet.setReps(newV);
            }
        });
        String[] weight = new String[200];
        for(int i = 0; i < weight.length; i++){
            weight[i] = Integer.toString(i);
        }
        holder.weightPicker.setDisplayedValues(weight);
        holder.weightPicker.setMinValue(0);
        holder.weightPicker.setMaxValue(weight.length - 1);
        holder.weightPicker.setWrapSelectorWheel(false);
        holder.weightPicker.setValue(currentSet.getWeight());
        holder.weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldV, int newV) {
                currentSet.setWeight(newV);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public int getLastRepCount(){
        if(sets.size() == 0){
            return 0;
        }
        return sets.get(getItemCount() - 1).getReps();
    }

    public int getLastWeightCount(){
        if(sets.size() == 0){
            return 0;
        }
        return sets.get(getItemCount() - 1).getWeight();
    }

}
