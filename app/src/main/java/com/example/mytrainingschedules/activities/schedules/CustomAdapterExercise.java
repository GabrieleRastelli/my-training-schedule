package com.example.mytrainingschedules.activities.schedules;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CustomAdapterExercise extends RecyclerView.Adapter<CustomAdapterExercise.ViewHolder> {

    private List<JSONObject> listItems;
    private Context context;

    public CustomAdapterExercise(List<JSONObject> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject exercise=listItems.get(position);
        try {
            holder.title.setText(exercise.getString("exercise-name"));
            holder.type.setText(exercise.getString("type"));
            String set = Integer.toString(exercise.getInt("sets"));
            String reps = Integer.toString(exercise.getInt("reps"));
            String setRep = set + " X " + reps;
            holder.set_rep.setText(setRep);
            holder.rest.setText(Integer.toString(exercise.getInt("rest-between-sets")));
            if (exercise.getString("equipment").equals("TRUE")) {
                holder.equipment.setBackgroundResource(R.drawable.dumbell);
            } else {
                holder.equipment.setBackgroundResource(R.drawable.emoji_emotions);
            }
            String peso = Integer.toString(exercise.getInt("weight")) + " KG ";
            holder.peso.setText(peso);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView type;
        public TextView set_rep;
        public TextView peso;
        public TextView rest;
        public TextView equipment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=(TextView) itemView.findViewById(R.id.textViewHead);
            type=(TextView) itemView.findViewById(R.id.textViewDesc);
            set_rep=(TextView) itemView.findViewById(R.id.set_rep);
            peso=(TextView) itemView.findViewById(R.id.peso);
            rest=(TextView) itemView.findViewById(R.id.rest);
            equipment=(TextView) itemView.findViewById(R.id.equipment);

        }
    }

    public List<JSONObject> getListItems() {
        return listItems;
    }

    public void setListItems(List<JSONObject> listItems) {
        this.listItems = listItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
