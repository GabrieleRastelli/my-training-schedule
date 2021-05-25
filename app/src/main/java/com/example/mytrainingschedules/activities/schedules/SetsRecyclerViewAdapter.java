package com.example.mytrainingschedules.activities.schedules;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Set;

import java.util.ArrayList;

public class SetsRecyclerViewAdapter extends RecyclerView.Adapter<SetsRecyclerViewAdapter.CustomViewHolder> {

    private ArrayList<Set> sets;
    private static RecyclerViewClickListener itemListener;

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView index, reps, weight;
        public TextView repAdd, repSub, weightAdd, weightSub;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.index);
            reps = itemView.findViewById(R.id.reps);
            weight = itemView.findViewById(R.id.weight);
            repAdd = itemView.findViewById(R.id.rep_add);
            repSub = itemView.findViewById(R.id.rep_sub);
            weightAdd = itemView.findViewById(R.id.weight_add);
            weightSub = itemView.findViewById(R.id.weight_sub);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, getLayoutPosition());
        }

    }

    public SetsRecyclerViewAdapter(ArrayList<Set> sets, RecyclerViewClickListener itemListener){
        this.sets = sets;
        SetsRecyclerViewAdapter.itemListener = itemListener;
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
        holder.reps.setText("Reps: " + currentSet.getReps());
        holder.weight.setText("Weight: " + currentSet.getWeight() + " kg");
        holder.repAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSet.setReps(currentSet.getReps() + 1);
                notifyDataSetChanged();
            }
        });
        holder.repSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentSet.getReps() > 0){
                    currentSet.setReps(currentSet.getReps() - 1);
                    notifyDataSetChanged();
                }
            }
        });
        holder.weightAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSet.setWeight(currentSet.getWeight() + 1);
                notifyDataSetChanged();
            }
        });
        holder.weightSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentSet.getWeight() > 0){
                    currentSet.setWeight(currentSet.getWeight() - 1);
                    notifyDataSetChanged();
                }
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
