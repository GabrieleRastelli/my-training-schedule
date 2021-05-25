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

public class ExerciseListViewAdapter extends ArrayAdapter<Exercise> {

    private int resourceLayout;
    private Context context;

    public ExerciseListViewAdapter(Context context, int resource, ArrayList<Exercise> exercises) {
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
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView category = (TextView) view.findViewById(R.id.category);
            TextView requiresEquipment = (TextView) view.findViewById(R.id.equipment);
            name.setText(exercise.getName());
            if(exercise.getCategory().equals("CHEST")){
                category.setBackground(this.context.getResources().getDrawable(R.drawable.chest_tag));
                category.setText("Chest");
            }
            if(exercise.requireEquipment()){
                requiresEquipment.setText("Equipment requires");
            }
            else{
                requiresEquipment.setText("Bodyweight exercise");
            }


        }

        return view;
    }

}
