package com.example.mytrainingschedules.activities.mainactivity.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;

import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter<Exercise> {

    private int resourceLayout;
    private Context context;

    public CustomListViewAdapter(Context context, int resource, ArrayList<Exercise> exercises) {
        super(context, resource, exercises);
        this.resourceLayout = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(resourceLayout, null);
        }

        Exercise exercise = getItem(position);

        if (exercise != null) {
            TextView exercise_name = (TextView) view.findViewById(R.id.exercise_name);
            TextView repsxsets = (TextView) view.findViewById(R.id.rxs);
            TextView weight = (TextView) view.findViewById(R.id.weight);
            exercise_name.setText(exercise.getName());
            repsxsets.setText(exercise.getReps() + " x " + exercise.getSets());
            if(exercise.getWeight() == 0){
                weight.setText("Bodyweight exercise");
            }
            else{
                weight.setText("Weight: " + exercise.getWeight() + " kg");
            }

        }

        return view;
    }

}
