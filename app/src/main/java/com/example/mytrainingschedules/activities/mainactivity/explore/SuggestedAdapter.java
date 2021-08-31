package com.example.mytrainingschedules.activities.mainactivity.explore;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.mainactivity.search.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * This adapter is used both for suggested and popular fragments.
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class SuggestedAdapter extends RecyclerView.Adapter<SuggestedAdapter.ViewHolder> {
    private Context context;
    private List<Schedule> data;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public void filterList (ArrayList<Schedule> filteredList){
        data=filteredList;
        notifyDataSetChanged();
    }

    public SuggestedAdapter(Context context, List<Schedule> data, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener=onItemClickListener;
        this.data = new ArrayList<Schedule>();
        for(int i = 0; i < data.size(); i++){
            this.data.add(data.get(i));
        }
        inflater = (LayoutInflater.from(context));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_schedule_layout,parent, false);

        return new ViewHolder(view,onItemClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule= data.get(position);

        holder.title.setText(schedule.getTitle());
        holder.description.setText(schedule.getDescription());
        String categoria1 = schedule.getCategoria1();
        if(!categoria1.isEmpty() && !categoria1.equals("null")){
            holder.categoria1.setText(categoria1);
            switch(categoria1){
                case "legs":
                case "LEGS":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.legs_tag));
                    holder.image.setBackgroundResource(R.drawable.man_doing_squats);
                    holder.cardView.setBackgroundResource(R.drawable.gradient_blue);
                    break;
                case "arms":
                case "ARMS":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.arms_tag));
                    holder.image.setBackgroundResource(R.drawable.man_doing_biceps);
                    holder.cardView.setBackgroundResource(R.drawable.gradient_red);
                    break;
                case "back":
                case "BACK":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.back_tag));
                    holder.image.setBackgroundResource(R.drawable.man_doing_back);
                    holder.cardView.setBackgroundResource(R.drawable.gradient_orange);
                    break;
                case "chest":
                case "CHEST":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.chest_tag));
                    holder.image.setBackgroundResource(R.drawable.man_doing_chest);
                    holder.cardView.setBackgroundResource(R.drawable.gradient_green);
                    break;
                case "shoulder":
                case "SHOULDER":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.shoulders_tag));
                    holder.image.setBackgroundResource(R.drawable.man_doing_shoulders);
                    holder.cardView.setBackgroundResource(R.drawable.gradient_yellow);
                    break;
                case "ABS":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.default_tag));
                    holder.image.setBackgroundResource(R.drawable.woman_doing_core);
                    holder.cardView.setBackgroundResource(R.drawable.gradient_default);
                    break;
                default:
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.default_tag));
                    break;
            }
        }

        String categoria2 = schedule.getCategoria2();
        if(!categoria2.isEmpty() && !categoria2.equals("null")){
            holder.categoria2.setText(categoria2);
            switch(categoria2){
                case "legs":
                case "LEGS":
                    holder.categoria2.setBackground(this.context.getResources().getDrawable(R.drawable.legs_tag));
                    break;
                case "arms":
                case "ARMS":
                    holder.categoria2.setBackground(this.context.getResources().getDrawable(R.drawable.arms_tag));
                    break;
                case "back":
                case "BACK":
                    holder.categoria2.setBackground(this.context.getResources().getDrawable(R.drawable.back_tag));
                    break;
                case "chest":
                case "CHEST":
                    holder.categoria2.setBackground(this.context.getResources().getDrawable(R.drawable.chest_tag));
                    break;
                case "shoulder":
                case "SHOULDER":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.shoulders_tag));
                default:
                    holder.categoria2.setBackground(this.context.getResources().getDrawable(R.drawable.default_tag));
                    break;
            }
        }

        String equipmentNeeded=schedule.getRequireEquipment();
        if(!equipmentNeeded.isEmpty() && !equipmentNeeded.equals("null")){
            switch(equipmentNeeded){
                case "true":
                case "TRUE":
                    holder.equipment.setBackground(this.context.getDrawable(R.drawable.dumbell));
                    //equipment.setBackground(this.context.getResources().getDrawable(R.drawable.dumbell));
                    break;
                case "false":
                case "FALSE":
                    holder.equipment.setBackgroundResource(R.color.transparent);
                    break;
                default:
                    holder.equipment.setBackground(this.context.getDrawable(R.drawable.dumbell));
                    break;
            }
        }
        String creator=schedule.getCreator();
        holder.creator.setText(context.getResources().getString(R.string.created_by)+creator);
        Integer downloads=schedule.getDownloads();
        holder.downloads.setText(String.valueOf(downloads)+" downloads");
    }



    @Override
    public int getItemCount() {
        return this.data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title, description, categoria1, categoria2, creator, equipment, downloads, image;
        CardView cardView;
        OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.workoutTitle);
            description = itemView.findViewById(R.id.workoutDescription);
            categoria1 = itemView.findViewById(R.id.exerciseCategory);
            categoria2 = itemView.findViewById(R.id.workoutCategory2);
            creator = itemView.findViewById(R.id.created_by);
            equipment = itemView.findViewById(R.id.equipment);
            downloads=itemView.findViewById(R.id.downloads);
            image=itemView.findViewById(R.id.image);
            cardView=itemView.findViewById(R.id.cardView);
            this.onItemClickListener=onItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getLayoutPosition());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
