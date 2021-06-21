package com.example.mytrainingschedules.activities.mainactivity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomAlertDialog;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.example.mytrainingschedules.activities.schedules.EditScheduleActivity;
import com.example.mytrainingschedules.activities.workout.RunningWorkoutActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewScheduleActivity extends AppCompatActivity {

    String guid, scheduleTitle, scheduleDescription;
    int scheduleId;
    Schedule schedule;
    TextView title, creator;
    ProgressBar progressBar;
    private RecyclerView listOfExercises;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FloatingActionButton deleteSchedule, playWorkout, editSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_schedule_layout);

        initGUI();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
            jsonObject.put("schedule", scheduleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getSchedule(getApplicationContext(), getResources().getString(R.string.base_url) + "/scheduleinfo", jsonObject);

    }

    private void initGUI(){
        title = findViewById(R.id.activityTitle);
        title.setText("");

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        creator=findViewById(R.id.creator);
        listOfExercises = findViewById(R.id.setsRecyclerView);
        listOfExercises.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listOfExercises.setLayoutManager(layoutManager);

        /* JSON object */
        guid = getIntent().getStringExtra("USER_GUID");
        scheduleId = getIntent().getIntExtra("SCHEDULE_ID", -1);
        scheduleTitle = getIntent().getStringExtra("SCHEDULE_TITLE");
        scheduleDescription = getIntent().getStringExtra("SCHEDULE_DESCRIPTION");

        /* start workout */
        playWorkout = findViewById(R.id.play);
        playWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewScheduleActivity.this, RunningWorkoutActivity.class);
                intent.putExtra("SCHEDULE", schedule);
                intent.putExtra("USER_GUID", guid);
                intent.putExtra("SCHEDULE_ID", scheduleId);
                ViewScheduleActivity.this.startActivity(intent);
            }
        });

        /* edit schedule */
        editSchedule = findViewById(R.id.edit);
        editSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewScheduleActivity.this, EditScheduleActivity.class);
                intent.putExtra("SCHEDULE", schedule);
                intent.putExtra("SCHEDULE_ID", scheduleId);
                intent.putExtra("USER_GUID", guid);
                ViewScheduleActivity.this.startActivity(intent);
            }
        });

        /* delete schedule */
        deleteSchedule = findViewById(R.id.delete);
        deleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject deleteJsonObject = new JSONObject();
                try {
                    deleteJsonObject.put("guid", guid);
                    deleteJsonObject.put("schedule", scheduleId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CustomAlertDialog alertDialog = new CustomAlertDialog(ViewScheduleActivity.this, "Delete schedule", "Are you sure you want to delete this schedule?");
                alertDialog.setListenerPositive(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSchedule(getApplicationContext(), getResources().getString(R.string.base_url) + "/deleteschedule", deleteJsonObject);
                        Intent intent = new Intent(ViewScheduleActivity.this, MainActivity.class);
                        intent.putExtra("USER_GUID", guid);
                        ViewScheduleActivity.this.startActivity(intent);
                        ViewScheduleActivity.this.finish();
                    }
                });

                runOnUiThread(alertDialog);
            }
        });
    }

    private void getSchedule(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);
        progressBar.setVisibility(View.VISIBLE);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonResponse = null;
                JSONObject result = null;
                JSONObject dataJson = null;
                JSONArray exercises = null;
                try {
                    jsonResponse = new JSONObject(response);
                    result = jsonResponse.getJSONObject("result");
                    String createdBy="Created by: "+result.getString("creator");
                    creator.setText(createdBy);
                    String dataJsonString=result.getString("dataJson");
                    Log.d("APP_DEBUG", dataJsonString);
                    dataJson = new JSONObject(dataJsonString);
                    exercises = dataJson.getJSONArray("exercises");
                    schedule = new Schedule(scheduleTitle, scheduleDescription, exercises);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("APP_DEBUG", e.toString());
                }

                title.setText(schedule.getTitle());
                recyclerViewAdapter = new CustomRecyclerViewAdapter(schedule.getExercises());
                listOfExercises.setAdapter(recyclerViewAdapter);
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("APP_DEBUG", "Fail: " + error.toString());
                /*errorTextView.setVisibility(View.VISIBLE);
                if (error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText("Can't connect to the server");
                } else if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    errorTextView.setText("Invalid credentials");
                } else {
                    errorTextView.setText("No Internet connection");
                } */
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }

    private void deleteSchedule(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);

                    Toast.makeText(context, "Schedule deleted successfully", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("APP_DEBUG", e.toString());
                }
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APP_DEBUG", "Fail: " + error.toString());
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }
}