package com.example.mytrainingschedules.activities.mainactivity.premium;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.example.mytrainingschedules.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleFilterAdapter extends BaseAdapter {

    private Context context;
    private JSONArray data;
    private LayoutInflater inflater;

    public ScheduleFilterAdapter(Context context){
        this.context = context;
        inflater = (LayoutInflater.from(context));
    }

    /* Compulsory Override methods. */
    @Override
    public int getCount(){
        return 6;
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
        view = inflater.inflate(R.layout.exercise_filter, null);
        TextView exType = (TextView) view.findViewById(R.id.exercise_type);
        TextView exImage = (TextView) view.findViewById(R.id.image_ex);
        CardView exBg = (CardView) view.findViewById(R.id.cardViewFilter);

        switch(i){
            case 0:
                exType.setText("Chest");
                exImage.setBackgroundResource(R.drawable.man_doing_chest);
                exBg.setBackgroundResource(R.drawable.gradient_green);
                break;
            case 1:
                exType.setText("Arms");
                exImage.setBackgroundResource(R.drawable.man_doing_biceps);
                exBg.setBackgroundResource(R.drawable.gradient_red);
                break;
            case 2:
                exType.setText("Back");
                exImage.setBackgroundResource(R.drawable.man_doing_back);
                exBg.setBackgroundResource(R.drawable.gradient_orange);
                break;
            case 3:
                exType.setText("Legs");
                exImage.setBackgroundResource(R.drawable.man_doing_squats);
                exBg.setBackgroundResource(R.drawable.gradient_blue);
                break;
            case 4:
                exType.setText("Shoulders");
                exImage.setBackgroundResource(R.drawable.man_doing_shoulders);
                exBg.setBackgroundResource(R.drawable.gradient_yellow);
                break;
            case 5:
                exType.setText("Free");
                exImage.setBackgroundResource(R.drawable.woman_doing_core);
                exBg.setBackgroundResource(R.drawable.gradient_default);
                break;
        }
        return view;
    }

}