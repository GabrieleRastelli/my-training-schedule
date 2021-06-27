package com.example.mytrainingschedules.activities.mainactivity.search;

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

/**
 * This activity is called by SearchFragment.
 * It displays a schedule to the user and eventually lets him download it to use it from his homepage.
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class DownloadScheduleActivity extends AppCompatActivity {

    private String TAG="DownloadScheduleActivity";
    private String guid, scheduleId, scheduleTitle, scheduleDescription;
    private com.example.mytrainingschedules.activities.Schedule schedule;
    private TextView title, creator, errorTextView;
    private ProgressBar progressBar;
    private RecyclerView listOfExercises;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button download;
    private Animation scaleDown, scaleUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_schedule_layout);

        initGui();

        try {
            Log.i(TAG, "Calling callScheduleInfo()");
            callScheduleInfo();
        } catch (JSONException je) {
            Log.e(TAG, "An error occurred while preparing scheduleinfo request body", je);
            Toast.makeText(this, "Can't get schedules, try later.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Method that instantiate GUI objects
     */
    private void initGui(){
        title = findViewById(R.id.activityTitle);
        title.setText("");

        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        errorTextView = findViewById(R.id.errorTextViewDownload);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);

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

        download = findViewById(R.id.download);
        /* the listener is used both for animation and for calling the downloadschedule endpoint */
        download.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    download.startAnimation(scaleDown);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    download.startAnimation(scaleUp);

                    try {
                        Log.i(TAG, "Calling callDownloadSchedule()");
                        callDownloadSchedule();
                    } catch (JSONException je) {
                        Log.e(TAG, "An error occurred while calling callDownloadSchedule", je);
                        Toast.makeText(getApplicationContext(), "Unable to save schedule", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    /**
     * Method that prepares request body and calls scheduleinfo endpoint
     * @throws JSONException
     */
    private void callScheduleInfo() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("guid", guid);
        jsonObject.put("schedule", scheduleId);

        /* get schedule info */
        getSchedule(getApplicationContext(), getResources().getString(R.string.base_url) + "/scheduleinfo", jsonObject);
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

                    title.setText(schedule.getTitle());
                    recyclerViewAdapter = new CustomRecyclerViewAdapter(schedule.getExercises());
                    listOfExercises.setAdapter(recyclerViewAdapter);
                } catch (JSONException e) {
                    Log.d(TAG,"An error occurred while parsing schedule returned from server", e);
                }
                Log.i(TAG, "Successfully got scheduleInfo");
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Fail in calling scheduleinfo endpoint: " + error.toString());
                errorTextView.setVisibility(View.VISIBLE);
                if (error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText("Can't connect to the server");
                } else if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    errorTextView.setText("Invalid credentials");
                } else {
                    errorTextView.setText("No Internet connection");
                }
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }

    /**
     * Method that prepares request body and calls downloadschedule endpoint
     * @throws JSONException
     */
    private void callDownloadSchedule() throws JSONException {
        JSONObject jsonObject= new JSONObject();

        jsonObject.put("guid",guid);
        jsonObject.put("schedule",scheduleId);

        /* we use a progressbar to let the user know that it's actually doing something. it gives a feedback */
        progressBar.setVisibility(View.VISIBLE);
        downloadSchedule(this, getResources().getString(R.string.base_url) + "/downloadschedule", jsonObject);
    }

    private void downloadSchedule(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Schedule saved, you can now find it in your schedules!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Successfully downloaded schedule");
            }
        };

        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "Fail in calling download schedule endpoint: " + error.toString());
                errorTextView.setVisibility(View.VISIBLE);
                if (error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText("Can't connect to the server");
                } else if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    errorTextView.setText("Invalid credentials");
                } else {
                    errorTextView.setText("No Internet connection");
                }
                Toast.makeText(getApplicationContext(), "Cannot save schedule, try later.", Toast.LENGTH_SHORT).show();
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }
}
