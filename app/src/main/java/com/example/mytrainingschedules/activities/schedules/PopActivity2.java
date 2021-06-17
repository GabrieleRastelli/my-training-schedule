package com.example.mytrainingschedules.activities.schedules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.mytrainingschedules.activities.Set;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.example.mytrainingschedules.activities.utils.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopActivity2 extends AppCompatActivity implements RecyclerViewClickListener {

    private RecyclerView setsRecyclerView;
    private SetsRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Set> sets;
    private SeekBar seekBar;
    private String title;
    //private int reps, sets, rest;
    private TextView restTextView;
    private float weight;
    private Schedule schedule;
    private int scheduleId;
    private String guid;
    private ArrayList<Exercise> exercises;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup2);

        title = getIntent().getStringExtra("EXERCISE_TITLE");
        setTitle(title);
        scheduleId = getIntent().getIntExtra("SCHEDULE_ID", -1);
        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        exercises = schedule.getExercises();
        int index = getIntent().getIntExtra("INDEX", -1);

        sets = new ArrayList<Set>();
        sets.add(new Set(8, 0));

        setsRecyclerView = findViewById(R.id.setsRecyclerView);
        setsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        setsRecyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new SetsRecyclerViewAdapter(sets, this);
        setsRecyclerView.setAdapter(recyclerViewAdapter);

        save = findViewById(R.id.save);
        /*save.setOnClickListener(new View.OnClickListener() {
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
        }); */

        initUI();

    }

    private void initUI(){
        restTextView = findViewById(R.id.rest);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                restTextView.setText("Rest: " + 5 + "s");
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
        Exercise currentExercise = exercises.get(position);
        Intent intent = new Intent(getApplicationContext(), PopActivity2.class);
        intent.putExtra("EXERCISE_TITLE", currentExercise.getName());
        intent.putExtra("SCHEDULE", schedule);
        intent.putExtra("USER_GUID", guid);
        intent.putExtra("SCHEDULE_ID", scheduleId);
        intent.putExtra("INDEX", position);
        startActivityForResult(intent, 0);
        //recyclerViewAdapter.notifyDataSetChanged();
    }
}