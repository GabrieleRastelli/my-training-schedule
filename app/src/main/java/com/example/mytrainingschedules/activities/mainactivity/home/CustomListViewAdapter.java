package com.example.mytrainingschedules.activities.mainactivity.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;

import java.util.ArrayList;
import java.util.List;

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
            TextView sets = (TextView) view.findViewById(R.id.sets);
            TextView reps = (TextView) view.findViewById(R.id.reps);
            TextView weight = (TextView) view.findViewById(R.id.weight);
            exercise_name.setText(exercise.getName());
            sets.setText("x " + exercise.getSets());
            reps.setText("Reps: " + exercise.getReps());
            weight.setText(exercise.getWeight() + " kg");
        }

        return view;
    }

}
