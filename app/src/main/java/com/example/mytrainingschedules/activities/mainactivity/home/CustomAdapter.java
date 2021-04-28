package com.example.mytrainingschedules.activities.mainactivity.home;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.mytrainingschedules.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private JSONArray data;
    private LayoutInflater inflater;

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

    /* Compulsory Override methods. */
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.workout_card_layout, null);
        TextView title = (TextView) view.findViewById(R.id.workoutTitle);
        TextView description = (TextView) view.findViewById(R.id.workoutDescription);
        TextView category = (TextView) view.findViewById(R.id.workoutCategory);
        TextView secondCategory = (TextView) view.findViewById(R.id.workoutCategory2);
        TextView equipment = (TextView) view.findViewById(R.id.equipment);
        try {
            JSONObject schedule=this.data.getJSONObject(i);
            title.setText(schedule.getString("title"));
            description.setText(schedule.getString("description"));
            String categoria1=schedule.getString("categoria1");
            if(!categoria1.isEmpty() && !categoria1.equals("null")){
                category.setText(categoria1);
                switch(categoria1){
                    case "legs":
                    case "LEGS":
                        category.setBackground(this.context.getResources().getDrawable(R.drawable.legs_tag));
                    break;
                    case "arms":
                    case "ARMS":
                        category.setBackground(this.context.getResources().getDrawable(R.drawable.arms_tag));
                    break;
                    case "back":
                    case "BACK":
                        category.setBackground(this.context.getResources().getDrawable(R.drawable.back_tag));
                    break;
                    case "chest":
                    case "CHEST":
                        category.setBackground(this.context.getResources().getDrawable(R.drawable.chest_tag));
                    break;
                    default:
                        category.setBackground(this.context.getResources().getDrawable(R.drawable.default_tag));
                    break;
                }
            }

            String categoria2=schedule.getString("categoria2");
            if(!categoria2.isEmpty() && !categoria2.equals("null")){
                secondCategory.setText(categoria2);
                switch(categoria2){
                    case "legs":
                    case "LEGS":
                        secondCategory.setBackground(this.context.getResources().getDrawable(R.drawable.legs_tag));
                        break;
                    case "arms":
                    case "ARMS":
                        secondCategory.setBackground(this.context.getResources().getDrawable(R.drawable.arms_tag));
                        break;
                    case "back":
                    case "BACK":
                        secondCategory.setBackground(this.context.getResources().getDrawable(R.drawable.back_tag));
                        break;
                    case "chest":
                    case "CHEST":
                        secondCategory.setBackground(this.context.getResources().getDrawable(R.drawable.chest_tag));
                        break;
                    default:
                        secondCategory.setBackground(this.context.getResources().getDrawable(R.drawable.default_tag));
                    break;
                }
            }

            String equipmentNeeded=schedule.getString("equipment");
            if(!equipmentNeeded.isEmpty() && !equipmentNeeded.equals("null")){
                switch(equipmentNeeded){
                    case "true":
                    case "TRUE":
                        equipment.setBackground(this.context.getDrawable(R.drawable.dumbell));
                        //equipment.setBackground(this.context.getResources().getDrawable(R.drawable.dumbell));
                        break;
                    case "false":
                    case "FALSE":
                        equipment.setBackground(this.context.getDrawable(R.drawable.emoji_emotions));
                        break;
                    default:
                        equipment.setBackground(this.context.getDrawable(R.drawable.dumbell));
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

}