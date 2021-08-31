package com.example.mytrainingschedules.activities.mainactivity.search;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This adapter is the one used in search schedules part
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Schedule> data;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    /**
     * This method is called from SearchFragment when user types in something in editText, sets the dataset to display to the one received
     * @param filteredList
     */
    public void filterList (ArrayList<Schedule> filteredList){
        data=filteredList;
        notifyDataSetChanged();
    }

    public RecyclerViewAdapter(Context context, List<Schedule> data, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener=onItemClickListener;
        this.data = new ArrayList<Schedule>();
        for(int i = 0; i < data.size(); i++){
            this.data.add(data.get(i));
        }
        inflater = (LayoutInflater.from(context));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_schedule_layout,parent, false);

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
                    break;
                case "arms":
                case "ARMS":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.arms_tag));
                    break;
                case "back":
                case "BACK":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.back_tag));
                    break;
                case "chest":
                case "CHEST":
                    holder.categoria1.setBackground(this.context.getResources().getDrawable(R.drawable.chest_tag));
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
                    break;
                case "false":
                case "FALSE":
                    holder.equipment.setBackground(null);
                    break;
                default:
                    holder.equipment.setBackground(null);
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

        public TextView title, description, categoria1, categoria2, creator, equipment, downloads;
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
