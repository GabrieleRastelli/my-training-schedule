package com.example.mytrainingschedules.activities.schedules;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mytrainingschedules.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SetExerciseDataActivity extends AppCompatActivity {

    private TextView exerciseTitle, restTextView, repsTextView, setsTextView, weightTextView;
    private FloatingActionButton rep_add, rep_sub, set_add, set_sub, weight_add, weight_sub;
    private SeekBar seekBar;
    private String title, category;
    private int reps, sets, weight, rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_exercise_data_layout);
        
        title = getIntent().getStringExtra("EXERCISE_TITLE");
        category = getIntent().getStringExtra("EXERCISE_CATEGORY");
        reps = 8;
        sets = 3;
        weight = 0;
        exerciseTitle = findViewById(R.id.exerciseTitle);
        exerciseTitle.setText(title);
        repsTextView = findViewById(R.id.reps);
        repsTextView.setText("Reps: " + reps);
        setsTextView = findViewById(R.id.sets);
        setsTextView.setText("Sets: " + sets);
        weightTextView = findViewById(R.id.weight);
        weightTextView.setText("Weight: " + weight + " kg");
        
        initButtons();

        restTextView = findViewById(R.id.rest);
        restTextView.setText("Rest: 120s");
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                restTextView.setText("Rest: " + i*5 + "s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    
    private void initButtons(){
        rep_add = findViewById(R.id.rep_add);
        rep_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reps < 100){
                    reps++;
                    repsTextView.setText("Reps: " + reps);
                }
            }
        });
        rep_sub = findViewById(R.id.rep_sub);
        rep_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reps > 1){
                    reps--;
                    repsTextView.setText("Reps: " + reps);
                }
            }
        });
        set_add = findViewById(R.id.set_add);
        set_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sets < 30){
                    sets++;
                    setsTextView.setText("Sets: " + sets);
                }
            }
        });
        set_sub = findViewById(R.id.set_sub);
        set_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sets > 1){
                    sets--;
                    setsTextView.setText("Sets: " + sets);
                }
            }
        });
        weight_add = findViewById(R.id.weight_add);
        weight_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(weight < 300){
                    weight++;
                    weightTextView.setText("Weight: " + weight + " kg");
                }
            }
        });
        weight_sub = findViewById(R.id.weight_sub);
        weight_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(weight > 1){
                    weight--;
                    weightTextView.setText("Weight: " + weight + " kg");
                }
            }
        });
    }
}