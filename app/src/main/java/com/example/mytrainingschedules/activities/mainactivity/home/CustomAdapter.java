package com.example.mytrainingschedules.activities.mainactivity.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mytrainingschedules.R;

import org.json.JSONArray;
import org.json.JSONException;

public class CustomAdapter extends BaseAdapter {

    Context context;
    JSONArray data;
    LayoutInflater inflater;

    public CustomAdapter(Context context, JSONArray data){
        this.context = context;
        this.data = new JSONArray();
        for(int i = 0; i < data.length(); i++){
            try {
                this.data.put(i, data.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        inflater = (LayoutInflater.from(context));
    }

    // compulsory
    @Override
    public int getCount(){
        return this.data.length();
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
        view = inflater.inflate(R.layout.workout_card_layout, null);
        TextView title = (TextView) view.findViewById(R.id.workoutTitle);
        TextView description = (TextView) view.findViewById(R.id.workoutDescription);
        TextView category = (TextView) view.findViewById(R.id.workoutCategory);
        try {
            title.setText(this.data.getJSONObject(i).getString("title"));
            description.setText(this.data.getJSONObject(i).getString("description"));
            if(i == 0){
                category.setText("Legs");
                category.setBackground(this.context.getResources().getDrawable(R.drawable.legs_tag));
            }
            if(i == 1){
                category.setText("Arms");
                category.setBackground(this.context.getResources().getDrawable(R.drawable.arms_tag));
            }
            if(i == 2){
                category.setText("Back");
                category.setBackground(this.context.getResources().getDrawable(R.drawable.back_tag));
            }
            if(i == 3){
                category.setText("Chest");
                category.setBackground(this.context.getResources().getDrawable(R.drawable.chest_tag));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

}