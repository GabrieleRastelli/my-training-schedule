package com.example.mytrainingschedules.activities.schedules;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.example.mytrainingschedules.activities.mainactivity.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytrainingschedules.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CompleteScheduleActivity extends AppCompatActivity {

    Button btn_save;
    private Animation scaleDown, scaleUp;
    EditText title,description;
    private boolean connectionAvailable;
    private TextView errorTextView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_schedule_layout);

        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        errorTextView = findViewById(R.id.errorTextView);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);
        connectionAvailable = false;




        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btn_save.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btn_save.startAnimation(scaleUp);

                    JSONObject jsonObject= new JSONObject();
                    try {
                        jsonObject.put("guid",getIntent().getStringExtra("guid"));
                        jsonObject.put("dataJson",getIntent().getStringExtra("dataJson"));
                        title= (EditText)findViewById(R.id.title);
                        jsonObject.put("title",title.getText().toString());
                        description= (EditText)findViewById(R.id.description);
                        jsonObject.put("description",description.getText().toString());
                        insertSchedule(getApplicationContext(), getResources().getString(R.string.base_url) + "/createschedule", jsonObject);


                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("USER_GUID",getIntent().getStringExtra("guid"));
                        startActivity(i);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Unable to save schedule", Toast.LENGTH_SHORT).show();
                    }



                }
                return true;
            }
        });

    }



    private void insertSchedule(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                connectionAvailable = true;
                Toast.makeText(getApplicationContext(), "Schedule saved, refresh the page to see it", Toast.LENGTH_SHORT).show();
            }
        };

        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                connectionAvailable = false;
                Log.d("APP_DEBUG", "Fail: " + error.toString());
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
}