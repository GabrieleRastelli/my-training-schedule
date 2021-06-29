package com.example.mytrainingschedules.activities.schedules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.Set;
import com.example.mytrainingschedules.activities.VolleyPostClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * AddExerciseActivity: used to add exercise to the schedule
 * Called by: EditScheduleActivity, CreateScheduleActivity
 * Layout: add_exercise_layout
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class AddExerciseActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private ArrayList<Exercise> exerciseList;
    private RecyclerView allExercises;
    private AddExerciseRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchbar;
    private TextView newExercise;
    private String selectedExerciseTitle, selectedExerciseCategory;
    private FloatingActionButton next;
    private ProgressBar progressBar;
    private Schedule schedule;
    private int scheduleId;
    private String guid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_layout);

        selectedExerciseTitle = null;
        selectedExerciseCategory = null;

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        scheduleId = getIntent().getIntExtra("SCHEDULE_ID", -1);

        exerciseList = new ArrayList<Exercise>();
        allExercises = findViewById(R.id.setsRecyclerView);
        allExercises.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        allExercises.setLayoutManager(layoutManager);
        recyclerViewAdapter = new AddExerciseRecyclerViewAdapter(exerciseList, this);

        String guid = getIntent().getStringExtra("USER_GUID");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* getExercises() */
        getExercises(getApplicationContext(), jsonObject);

        /* searchbar filter */
        searchbar = findViewById(R.id.searchbar);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recyclerViewAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedExerciseTitle == null || selectedExerciseCategory == null){
                    Toast.makeText(getApplicationContext(), "Please select an exercise, or create one", Toast.LENGTH_LONG).show();
                }
                else{
                    Exercise exercise = new Exercise(selectedExerciseTitle, new ArrayList<Set>(), 0, 0, selectedExerciseCategory);
                    schedule.addExercise(exercise);
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
            }
        });

        newExercise = findViewById(R.id.newExercise);
        newExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateExerciseActivity.class);
                intent.putExtra("SCHEDULE", schedule);
                intent.putExtra("USER_GUID", guid);
                intent.putExtra("SCHEDULE_ID", scheduleId);
                startActivity(intent);
            }
        });

    }

    private void getExercises(Context context, JSONObject jsonObject) {
        progressBar.setVisibility(View.VISIBLE);

        VolleyPostClient client = new VolleyPostClient(context, "/exercise", jsonObject);
        client.setProgressBar(progressBar);
        client.setDefaultErrorListener();

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray result = jsonResponse.getJSONArray("result");
                    for(int i = 0; i < result.length(); i++){
                        JSONObject exercise = result.getJSONObject(i);
                        String name = exercise.getString("title");
                        String category = exercise.getString("category");
                        exerciseList.add(new Exercise(name, category, false));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerViewAdapter.setData(exerciseList);
                allExercises.setAdapter(recyclerViewAdapter);
            }
        };
        client.setOnSuccessListener(onSuccessListener);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(client.getStringRequest());
    }

    @Override
    public void recyclerViewListClicked(View view, int position) {
        TextView checkIcon = view.findViewById(R.id.checkIcon);
        if(checkIcon.getVisibility() == View.VISIBLE){
            selectedExerciseTitle = null;
            selectedExerciseCategory = null;
        }
        else{
            TextView exerciseTitleTextView = view.findViewById(R.id.activityTitle);
            selectedExerciseTitle = exerciseTitleTextView.getText().toString();
            TextView exerciseCategoryTextView = view.findViewById(R.id.exerciseCategory);
            selectedExerciseCategory = exerciseCategoryTextView.getText().toString();
        }
        recyclerViewAdapter.setSelectedTitle(selectedExerciseTitle);
        recyclerViewAdapter.setSelectedCategory(selectedExerciseCategory);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}