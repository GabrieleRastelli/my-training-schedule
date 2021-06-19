package com.example.mytrainingschedules.activities.mainactivity.settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.mainactivity.home.CustomRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadScheduleActivity extends AppCompatActivity {

    String guid, scheduleId, scheduleTitle, scheduleDescription;
    com.example.mytrainingschedules.activities.Schedule schedule;
    TextView title, creator;
    ProgressBar progressBar;
    private RecyclerView listOfExercises;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Button download;
    private Animation scaleDown, scaleUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_schedule_layout);

        initGUI();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
            jsonObject.put("schedule", scheduleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* get schedule info */
        getSchedule(getApplicationContext(), getResources().getString(R.string.base_url) + "/scheduleinfo", jsonObject);


    }

    private void initGUI(){
        title = findViewById(R.id.activityTitle);
        title.setText("");

        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        download = findViewById(R.id.download);

        download.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    download.startAnimation(scaleDown);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    download.startAnimation(scaleUp);
                    JSONObject jsonObject= new JSONObject();
                    try {
                        jsonObject.put("guid",guid);
                        jsonObject.put("schedule",scheduleId);
                        downloadSchedule(getApplicationContext(), getResources().getString(R.string.base_url) + "/downloadschedule", jsonObject);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Unable to save schedule", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        creator=findViewById(R.id.creator);
        listOfExercises = findViewById(R.id.setsRecyclerView);
        listOfExercises.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listOfExercises.setLayoutManager(layoutManager);

        /* JSON object */
        guid = getIntent().getStringExtra("USER_GUID");
        scheduleId = getIntent().getStringExtra("SCHEDULE_ID");
        scheduleTitle = getIntent().getStringExtra("SCHEDULE_TITLE");
        scheduleDescription = getIntent().getStringExtra("SCHEDULE_DESCRIPTION");

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

    private void downloadSchedule(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Schedule saved, you can now find it in your schedules!", Toast.LENGTH_SHORT).show();
            }
        };

        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*connectionAvailable = false;
                Log.d("APP_DEBUG", "Fail: " + error.toString());
                errorTextView.setVisibility(View.VISIBLE);
                if (error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText("Can't connect to the server");
                } else if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    errorTextView.setText("Invalid credentials");
                } else {
                    errorTextView.setText("No Internet connection");
                }*/
                Toast.makeText(getApplicationContext(), "Cannot save schedule, try later.", Toast.LENGTH_SHORT).show();
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }
}
