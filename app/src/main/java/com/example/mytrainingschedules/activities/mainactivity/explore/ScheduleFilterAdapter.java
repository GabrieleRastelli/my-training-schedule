package com.example.mytrainingschedules.activities.mainactivity.explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.mytrainingschedules.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * This adapter is used for the filtering buttons.
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class ScheduleFilterAdapter extends BaseAdapter {

    private Context context;
    private JSONArray data;
    private LayoutInflater inflater;
    private Map<Integer, Boolean> clickedFilter;
    private boolean clickedPerformed=false;
    private boolean endedInit=false;

    public ScheduleFilterAdapter(Context context){
        this.context = context;
        clickedFilter=new HashMap<>();
        /* non è stato clickato nessun filtro */
        for(Integer i=0;i<6;i++) {
            clickedFilter.put(i, false);
        }
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



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.exercise_filter, null);


        TextView exType = (TextView) view.findViewById(R.id.exercise_type);
        TextView exImage = (TextView) view.findViewById(R.id.image_ex);
        CardView exBg = (CardView) view.findViewById(R.id.cardViewFilter);

        switch(i){
            case 0:
                exType.setText("Chest");
                exBg.setBackgroundResource(R.drawable.gradient_green);
                exImage.setBackgroundResource(R.drawable.man_doing_chest);
                break;
            case 1:
                exType.setText("Arms");
                exBg.setBackgroundResource(R.drawable.gradient_red);
                exImage.setBackgroundResource(R.drawable.man_doing_biceps);
                break;
            case 2:
                exType.setText("Back");
                exBg.setBackgroundResource(R.drawable.gradient_orange);
                exImage.setBackgroundResource(R.drawable.man_doing_back);
                break;
            case 3:
                exType.setText("Legs");
                exBg.setBackgroundResource(R.drawable.gradient_blue);
                exImage.setBackgroundResource(R.drawable.man_doing_squats);
                break;
            case 4:
                exType.setText("Shoulders");
                exBg.setBackgroundResource(R.drawable.gradient_yellow);
                exImage.setBackgroundResource(R.drawable.man_doing_shoulders);
                break;
            case 5:
                exType.setText("Free");
                exBg.setBackgroundResource(R.drawable.gradient_default);
                exImage.setBackgroundResource(R.drawable.woman_doing_core);
                break;
        }
        return view;
    }

    public boolean isEnabled(int _position){
        return true;
    }

    public interface GridViewItemClickListener{
        void onGridItemClick(View v, int index);
    }

}