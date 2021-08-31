package com.example.mytrainingschedules.activities.schedules;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * CreateExerciseActivity: used to create a new exercise
 * Called by: AddExerciseActivity
 * Layout: create_exercise_layout
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class CreateExerciseActivity extends AppCompatActivity {

    private FloatingActionButton done;
    private EditText nameEditText;
    private Spinner categorySpinner;
    private Schedule schedule;
    private String guid;
    private int scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_exercise_layout);

        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        scheduleId = getIntent().getIntExtra("SCHEDULE_ID", -1);

        nameEditText = findViewById(R.id.nameEditText);
        categorySpinner = findViewById(R.id.categorySpinner);

        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                if(name.equals("") || name == null){
                    Toast.makeText(getApplicationContext(), getString(R.string.insert_ex_name), Toast.LENGTH_SHORT).show();
                }
                else{
                    String category = categorySpinner.getSelectedItem().toString().toUpperCase();
                    Exercise exercise = new Exercise(name, new ArrayList<Set>(), 0, 0, category);
                    schedule.addExercise(exercise);
                    Intent intent;
                    if(scheduleId == 0){
                        intent = new Intent(getApplicationContext(), CreateScheduleActivity.class);
                    }
                    else{
                        intent = new Intent(getApplicationContext(), EditScheduleActivity.class);
                    }
                    intent.putExtra("SCHEDULE", schedule);
                    intent.putExtra("USER_GUID", guid);
                    intent.putExtra("SCHEDULE_ID", scheduleId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}