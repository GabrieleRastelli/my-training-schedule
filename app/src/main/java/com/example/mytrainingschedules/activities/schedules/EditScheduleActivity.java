package com.example.mytrainingschedules.activities.schedules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class EditScheduleActivity extends AppCompatActivity {

    private RecyclerView listOfExercises;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Schedule schedule;
    private int scheduleId;
    private String guid;
    private ArrayList<Exercise> exercises;
    FloatingActionButton add, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_schedule_layout);

        scheduleId = Integer.parseInt(getIntent().getStringExtra("SCHEDULE_ID"));
        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        exercises = schedule.getExercises();

        listOfExercises = findViewById(R.id.allExercises);
        listOfExercises.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listOfExercises.setLayoutManager(layoutManager);
        recyclerViewAdapter = new CustomAdapterExercise2(exercises);
        listOfExercises.setAdapter(recyclerViewAdapter);

        /* drag and drop items in recycler view */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int positionDragged = dragged.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                Collections.swap(exercises, positionDragged, positionTarget);
                recyclerViewAdapter.notifyItemMoved(positionDragged, positionTarget);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                /*TODO: non va*/
                int position = viewHolder.getAdapterPosition();
                exercises.remove(position);
                recyclerViewAdapter.notifyItemRemoved(position);
            }
        });
        helper.attachToRecyclerView(listOfExercises);

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddExerciseActivity.class);
                intent.putExtra("USER_GUID", guid);
                startActivityForResult(intent, 0);
            }
        });

    }
}