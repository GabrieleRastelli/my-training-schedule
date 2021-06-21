package com.example.mytrainingschedules.activities.schedules;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class SetScheduleDataActivity extends AppCompatActivity {

    /*
     * SetScheduleDataActivity: used to give the schedule a title and a description
     * Called by: CreateScheduleActivity
     * Layout: set_schedule_data_layout
     */

    private Schedule schedule;
    private String guid;
    private EditText titleEditText, descriptionEditText;
    private FloatingActionButton done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_schedule_data_layout);

        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getResources().getString(R.string.base_url) + "/createschedule";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("guid", guid);
                    jsonObject.put("title", titleEditText.getText());
                    jsonObject.put("description", descriptionEditText.getText());
                    jsonObject.put("dataJson", schedule.getJsonExercises());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                createSchedule(getApplicationContext(), url, jsonObject);
            }
        });

    }

    private void createSchedule(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Schedule succesfully created", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("USER_GUID", guid);
                startActivity(intent);
                finish();
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