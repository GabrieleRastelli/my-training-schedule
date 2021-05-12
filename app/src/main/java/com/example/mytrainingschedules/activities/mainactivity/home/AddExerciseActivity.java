package com.example.mytrainingschedules.activities.mainactivity.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Exercise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddExerciseActivity extends AppCompatActivity {

    String guid;
    ProgressBar progressBar;
    ListView listOfExercises;
    ExerciseListViewAdapter adapter;
    ArrayList<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_layout);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        listOfExercises = findViewById(R.id.allExercises);

        exercises = new ArrayList<Exercise>();

        /* JSON object */
        guid = getIntent().getStringExtra("USER_GUID");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* get exercises */
        getExercises(getApplicationContext(), getResources().getString(R.string.base_url) + "/exercise", jsonObject);
    }

    private void getExercises(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);
        progressBar.setVisibility(View.VISIBLE);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonResponse = null;
                JSONArray result = null;
                JSONObject exercise = null;
                try {
                    jsonResponse = new JSONObject(response);
                    result = jsonResponse.getJSONArray("result");
                    for(int i = 0; i < result.length(); i++){
                        exercise = result.getJSONObject(i);
                        String name = exercise.getString("title");
                        String category = exercise.getString("category");
                        boolean requiresEquipment = exercise.getBoolean("equipment");
                        exercises.add(new Exercise(name, category, requiresEquipment));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("APP_DEBUG", e.toString());
                }

                adapter = new ExerciseListViewAdapter(getApplicationContext(), R.layout.exercise_row_layout_list, exercises);
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

}