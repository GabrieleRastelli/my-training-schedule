package com.example.mytrainingschedules.activities.schedules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;

import java.util.List;

public class CustomAdapterExercise extends RecyclerView.Adapter<CustomAdapterExercise.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    public CustomAdapterExercise(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem listItem=listItems.get(position);

        holder.head.setText(listItem.getHead());
        holder.desc.setText(listItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView head;
        public TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            head=(TextView) itemView.findViewById(R.id.textViewHead);
            desc=(TextView) itemView.findViewById(R.id.textViewDesc);

        }
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
