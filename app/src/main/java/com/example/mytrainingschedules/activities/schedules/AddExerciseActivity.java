package com.example.mytrainingschedules.activities.schedules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.example.mytrainingschedules.activities.utils.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddExerciseActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private ArrayList<Exercise> exerciseList;
    private RecyclerView allExercises;
    private CustomRecyclerViewAdapterExercises recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchbar;
    private String selectedExerciseTitle, selectedExerciseCategory;
    private FloatingActionButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_exercise2);

        selectedExerciseTitle = null;
        selectedExerciseCategory = null;

        exerciseList = new ArrayList<Exercise>();
        allExercises = findViewById(R.id.allExercises);
        allExercises.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        allExercises.setLayoutManager(layoutManager);
        recyclerViewAdapter = new CustomRecyclerViewAdapterExercises(exerciseList, this);

        String guid = getIntent().getStringExtra("USER_GUID");
        String url = getResources().getString(R.string.base_url) + "/exercise";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /* getExercises() function. */
        getExercises(getApplicationContext(), url, jsonObject);

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
                    // start new activity
                }
            }
        });

    }

    private void getExercises(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

    @Override
    public void recyclerViewListClicked(View view, int position) {
        TextView checkIcon = view.findViewById(R.id.checkIcon);
        if(checkIcon.getVisibility() == View.VISIBLE){
            selectedExerciseTitle = null;
            selectedExerciseCategory = null;
        }
        else{
            TextView exerciseTitleTextView = view.findViewById(R.id.exerciseTitle);
            selectedExerciseTitle = exerciseTitleTextView.getText().toString();
            TextView exerciseCategoryTextView = view.findViewById(R.id.exerciseCategory);
            selectedExerciseCategory = exerciseCategoryTextView.getText().toString();
        }
        recyclerViewAdapter.setSelectedTitle(selectedExerciseTitle);
        recyclerViewAdapter.setSelectedCategory(selectedExerciseCategory);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}