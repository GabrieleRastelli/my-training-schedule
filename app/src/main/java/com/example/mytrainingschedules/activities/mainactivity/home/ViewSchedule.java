package com.example.mytrainingschedules.activities.mainactivity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
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
import com.example.mytrainingschedules.activities.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewSchedule extends AppCompatActivity {

    String guid, scheduleId, scheduleTitle, scheduleDescription;
    Schedule schedule;
    TextView title;
    ProgressBar progressBar;
    CustomListViewAdapter adapter;
    ListView listOfExercises;
    FloatingActionButton deleteSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_schedule_layout);

        title = findViewById(R.id.title);
        title.setText("");

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        listOfExercises = findViewById(R.id.listOfExercises);

        /* JSON object */
        guid = getIntent().getStringExtra("USER_GUID");
        scheduleId = getIntent().getStringExtra("SCHEDULE_ID");
        scheduleTitle = getIntent().getStringExtra("SCHEDULE_TITLE");
        scheduleDescription = getIntent().getStringExtra("SCHEDULE_DESCRIPTION");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
            jsonObject.put("schedule", scheduleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* get schedule info */
        getSchedule(getApplicationContext(), getResources().getString(R.string.base_url) + "/scheduleinfo", jsonObject);

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
                /* alert dialog */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(ViewSchedule.this)
                                    .setTitle("Your Alert")
                                    .setMessage("Your Message")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteSchedule(getApplicationContext(), getResources().getString(R.string.base_url) + "/deleteschedule", deleteJsonObject);
                                        }
                                    }).show();
                        }
                    }
                });
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
                    String dataJsonString=result.getString("dataJson");
                    dataJson = new JSONObject(dataJsonString);
                    exercises=dataJson.getJSONArray("exercises");
                    schedule = new Schedule(scheduleTitle, scheduleDescription, exercises);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("APP_DEBUG", e.toString());
                }

                title.setText(schedule.getTitle());
                adapter = new CustomListViewAdapter(getApplicationContext(), R.layout.exercise_row_layout, schedule.getExercises());
                listOfExercises.setAdapter(adapter);

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