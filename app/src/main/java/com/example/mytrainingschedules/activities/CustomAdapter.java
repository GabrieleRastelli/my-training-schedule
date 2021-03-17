package com.example.mytrainingschedules.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mytrainingschedules.R;

public class CustomAdapter extends BaseAdapter {

    Context context;
    String[] data;
    LayoutInflater inflater;

    public CustomAdapter(Context context, String[] data){
        this.context = context;
        this.data = new String[9];
        for(int i = 0; i < 9; i++){
            this.data[i] = data[i];
        }
        inflater = (LayoutInflater.from(context));
    }

    // compulsory
    @Override
    public int getCount(){
        return this.data.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.workout_card_layout, null); // inflate the layout
        TextView title = (TextView) view.findViewById(R.id.workoutTitle); // get the reference of TextView
        title.setText(this.data[i]); // set text
        return view;
    }

}