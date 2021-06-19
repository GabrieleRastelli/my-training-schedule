package com.example.mytrainingschedules.activities.schedules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class EditExerciseActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private RecyclerView setsRecyclerView;
    private SetsRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String title, category, guid;
    private int rest;
    private Exercise exercise;
    private ArrayList<Set> sets;
    private TextView startingMessage, titleTextView, restTextView;
    private SeekBar seekBar;
    private FloatingActionButton save;
    private Button addset;
    private Schedule schedule;
    private int scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* TODO: far si che questa activity sia un PopUp */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_exercise_data_layout);

        startingMessage = findViewById(R.id.msg);
        startingMessage.setVisibility(View.GONE);

        titleTextView = findViewById(R.id.activityTitle);
        titleTextView = findViewById(R.id.activityTitle);
        title = getIntent().getStringExtra("EXERCISE_TITLE");
        titleTextView.setText(title);
        scheduleId = getIntent().getIntExtra("SCHEDULE_ID", -1);
        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        ArrayList<Exercise> exercises = schedule.getExercises();
        int index = getIntent().getIntExtra("INDEX", -1);

        exercise = exercises.get(index);
        sets = exercise.getSets();

        setsRecyclerView = findViewById(R.id.setsRecyclerView);
        setsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        setsRecyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new SetsRecyclerViewAdapter(sets, this);
        setsRecyclerView.setAdapter(recyclerViewAdapter);

        addset = findViewById(R.id.add);
        addset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startingMessage.setVisibility(View.GONE);
                sets.add(new Set(recyclerViewAdapter.getLastRepCount(), recyclerViewAdapter.getLastWeightCount()));
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise.setSets(sets);
                exercise.setRest(rest);
                Intent intent;
                if(scheduleId == 0){
                    intent = new Intent(getApplicationContext(), CreateScheduleActivity.class);
                }
                else{
                    intent = new Intent(getApplicationContext(), EditScheduleActivity.class);
                }
                intent.putExtra("SCHEDULE", schedule);
                intent.putExtra("USER_GUID", guid);
                intent.putExtra("SCHEDULE_ID", scheduleId);
                startActivity(intent);
            }
        });

        /* drag and drop items in recycler view */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int positionDragged = dragged.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                Collections.swap(sets, positionDragged, positionTarget);
                recyclerViewAdapter.notifyItemMoved(positionDragged, positionTarget);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                sets.remove(position);
                recyclerViewAdapter.notifyItemRemoved(position);
            }
        });
        helper.attachToRecyclerView(setsRecyclerView);

        initUI();

    }

    private void initUI(){
        restTextView = findViewById(R.id.rest);
        rest = exercise.getRest();
        restTextView.setText("Rest: " + rest + "s");

        seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(rest / 5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                rest = i * 5;
                restTextView.setText("Rest: " + rest + "s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void recyclerViewListClicked(View view, int position) {
        // nothing
    }

}