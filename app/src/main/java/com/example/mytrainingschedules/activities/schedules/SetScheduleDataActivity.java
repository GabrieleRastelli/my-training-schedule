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
import com.example.mytrainingschedules.activities.VolleyPostClient;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SetScheduleDataActivity: used to give the schedule a title and a description
 * Called by: CreateScheduleActivity
 * Layout: set_schedule_data_layout
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class SetScheduleDataActivity extends AppCompatActivity {

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
                if(titleEditText.getText().equals("") || titleEditText.getText() == null){
                    Toast.makeText(getApplicationContext(), "Please insert schedule title!", Toast.LENGTH_SHORT).show();
                }
                else{
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("guid", guid);
                        jsonObject.put("title", titleEditText.getText());
                        jsonObject.put("description", descriptionEditText.getText());
                        jsonObject.put("dataJson", schedule.getJsonExercises());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    createSchedule(getApplicationContext(), jsonObject);
                }
            }
        });

    }

    private void createSchedule(Context context, JSONObject jsonObject) {
        VolleyPostClient client = new VolleyPostClient(context, "/createschedule", jsonObject);
        client.setDefaultErrorListener();

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
        client.setOnSuccessListener(onSuccessListener);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(client.getStringRequest());
    }

}