package com.example.mytrainingschedules.activities.schedules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SetExerciseDataActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private RecyclerView setsRecyclerView;
    private SetsRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String title, category, guid;
    private int rest;
    private ArrayList<Set> sets;
    private TextView startingMessage, titleTextView, restTextView;
    private SeekBar seekBar;
    private FloatingActionButton save;
    private NumberPicker numberPicker;
    private Button addset;
    private Schedule schedule;
    private int scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_exercise_data_layout);

        sets = new ArrayList<Set>();

        startingMessage = findViewById(R.id.msg);
        startingMessage.setVisibility(View.VISIBLE);

        titleTextView = findViewById(R.id.activityTitle);
        title = getIntent().getStringExtra("EXERCISE_TITLE");
        titleTextView.setText(title);
        category = getIntent().getStringExtra("EXERCISE_CATEGORY");
        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        ArrayList<Exercise> exercises = schedule.getExercises();

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
                Exercise exercise = new Exercise(title, sets, rest, 0, category);
                schedule.addExercise(exercise);
                Intent intent = new Intent(getApplicationContext(), EditScheduleActivity.class);
                intent.putExtra("SCHEDULE", schedule);
                intent.putExtra("USER_GUID", guid);
                intent.putExtra("SCHEDULE_ID", scheduleId);
                startActivity(intent);
            }
        });

        initUI();

    }

    private void initUI(){

        rest = 60;
        numberPicker = findViewById(R.id.numberPicker);
        String[] seconds = new String[600/5];
        for(int i = 0; i < seconds.length; i++){
            seconds[i] = Integer.toString(i * 5 + 5);
        }
        numberPicker.setDisplayedValues(seconds);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(seconds.length - 1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(rest);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldV, int newV) {
                rest = newV * 5 + 5;
            }
        });

        restTextView = findViewById(R.id.rest);
        //rest = 60;
        //restTextView.setText("Rest: " + rest + "s");
        
       /* seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(12);
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
        }); */
    }

    @Override
    public void recyclerViewListClicked(View view, int position) {
        // nothing
    }
}