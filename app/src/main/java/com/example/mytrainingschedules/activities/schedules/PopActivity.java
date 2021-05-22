package com.example.mytrainingschedules.activities.schedules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.example.mytrainingschedules.activities.utils.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopActivity extends AppCompatActivity {

    private TextView restTextView, repsTextView, setsTextView, weightTextView;
    private FloatingActionButton rep_add, rep_sub, set_add, set_sub, weight_add, weight_sub;
    private SeekBar seekBar;
    private String title;
    private int reps, sets, rest;
    private float weight;
    private Schedule schedule;
    private int scheduleId;
    private String guid;
    private ArrayList<Exercise> exercises;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_exercise);

        title = getIntent().getStringExtra("EXERCISE_TITLE");
        setTitle(title);
        scheduleId = getIntent().getIntExtra("SCHEDULE_ID", -1);
        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        exercises = schedule.getExercises();
        int index = getIntent().getIntExtra("INDEX", -1);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exercises.get(index).setReps(reps);
                exercises.get(index).setSets(sets);
                exercises.get(index).setWeight(weight);
                if(weight != 0){
                    exercises.get(index).setRequireEquipment(true);
                }
                else{
                    exercises.get(index).setRequireEquipment(false);
                }
                exercises.get(index).setRest_between_sets(rest);

                schedule.setExercises(exercises);
                Intent intent = new Intent(getApplicationContext(), EditScheduleActivity.class);
                intent.putExtra("SCHEDULE", schedule);
                intent.putExtra("USER_GUID", guid);
                intent.putExtra("SCHEDULE_ID", scheduleId);
                startActivity(intent);
            }
        });

        reps = exercises.get(index).getReps();
        sets = exercises.get(index).getSets();
        weight = exercises.get(index).getWeight();
        rest = exercises.get(index).getRest_between_sets();

        initUI();

    }

    private void initUI(){
        repsTextView = findViewById(R.id.reps);
        repsTextView.setText("Reps: " + reps);
        setsTextView = findViewById(R.id.sets);
        setsTextView.setText("Sets: " + sets);
        weightTextView = findViewById(R.id.weight);
        weightTextView.setText("Weight: " + weight + " kg");
        restTextView = findViewById(R.id.rest);
        restTextView.setText("Rest: " + rest + "s");
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
}