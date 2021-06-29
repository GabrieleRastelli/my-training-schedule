package com.example.mytrainingschedules.activities.schedules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomAlertDialog;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.VolleyPostClient;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * EditScheduleActivity: used to edit a schedule
 * Called by: ViewScheduleActivity
 * Layout: edit_schedule_layout
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class EditScheduleActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private RecyclerView listOfExercises;
    private EditScheduleRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Schedule schedule;
    private int scheduleId;
    private String guid, title, description;
    private ArrayList<Exercise> exercises;
    private FloatingActionButton add, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_schedule_layout);

        scheduleId = getIntent().getIntExtra("SCHEDULE_ID", -1);
        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        title = schedule.getTitle();
        description = schedule.getDescription();
        exercises = schedule.getExercises();

        listOfExercises = findViewById(R.id.setsRecyclerView);
        listOfExercises.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listOfExercises.setLayoutManager(layoutManager);
        recyclerViewAdapter = new EditScheduleRecyclerViewAdapter(exercises, this);
        listOfExercises.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.notifyDataSetChanged();

        /* drag and drop items in recycler view */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int positionDragged = dragged.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                Collections.swap(exercises, positionDragged, positionTarget);
                recyclerViewAdapter.notifyItemMoved(positionDragged, positionTarget);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                exercises.remove(position);
                recyclerViewAdapter.notifyItemRemoved(position);
            }
        });
        helper.attachToRecyclerView(listOfExercises);

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddExerciseActivity.class);
                intent.putExtra("SCHEDULE", schedule);
                intent.putExtra("USER_GUID", guid);
                intent.putExtra("SCHEDULE_ID", scheduleId);
                startActivityForResult(intent, 0);
            }
        });

        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("guid", guid);
                    jsonObject.put("schedule", scheduleId);
                    jsonObject.put("title", title);
                    jsonObject.put("description", description);
                    jsonObject.put("dataJson", schedule.getJsonExercises());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editSchedule(getApplicationContext(), jsonObject);
            }
        });

    }

    @Override
    public void recyclerViewListClicked(View view, int position) {
        Exercise currentExercise = exercises.get(position);
        Intent intent = new Intent(getApplicationContext(), EditExerciseActivity.class);
        intent.putExtra("EXERCISE_TITLE", currentExercise.getName());
        intent.putExtra("SCHEDULE", schedule);
        intent.putExtra("USER_GUID", guid);
        intent.putExtra("SCHEDULE_ID", scheduleId);
        intent.putExtra("INDEX", position);
        startActivityForResult(intent, 0);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void editSchedule(Context context, JSONObject jsonObject) {
        VolleyPostClient client = new VolleyPostClient(context, "/updateschedule", jsonObject);
        client.setDefaultErrorListener();

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Schedule succesfully updated", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        CustomAlertDialog alertDialog = new CustomAlertDialog(EditScheduleActivity.this, "Exit", "Attention: all progress will be lost");
        alertDialog.setListenerPositive(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditScheduleActivity.this.finish();
            }
        });
        runOnUiThread(alertDialog);
    }

}