package com.example.mytrainingschedules.activities.workout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.mainactivity.home.ViewSchedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RunningWorkoutActivity extends AppCompatActivity {

    private Schedule schedule;
    private TextView initTimer, exerciseName, reps, weight, sets, rest;
    private FloatingActionButton nextExercise, addSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_layout);

        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");

        initGUI();

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                initTimer.setText("Workout starting in...\n" + millisUntilFinished / 1000);
            }
            public void onFinish() {
                initTimer.setVisibility(View.GONE);
                setElementsVisible();
                startExercise(0, 0);
            }
        }.start();
    }

    private void initGUI(){
        exerciseName = findViewById(R.id.exerciseName);
        exerciseName.setVisibility(View.GONE);
        reps = findViewById(R.id.reps);
        reps.setVisibility(View.GONE);
        weight = findViewById(R.id.weight);
        weight.setVisibility(View.GONE);
        sets = findViewById(R.id.sets);
        sets.setVisibility(View.GONE);
        rest = findViewById(R.id.rest);
        rest.setVisibility(View.GONE);
        nextExercise = findViewById(R.id.next);
        nextExercise.setVisibility(View.GONE);
        addSet = findViewById(R.id.addSet);
        addSet.setVisibility(View.GONE);
        initTimer = findViewById(R.id.initTimer);
        initTimer.setVisibility(View.VISIBLE);
    }

    private void setElementsVisible(){
        exerciseName.setVisibility(View.VISIBLE);
        reps.setVisibility(View.VISIBLE);
        weight.setVisibility(View.VISIBLE);
        sets.setVisibility(View.VISIBLE);
        nextExercise.setVisibility(View.VISIBLE);
        addSet.setVisibility(View.VISIBLE);
    }

    private void startExercise(int index, int doneSets){
        Exercise currentExercise = schedule.getExercises().get(index);
        rest.setVisibility(View.GONE);
        exerciseName.setText(currentExercise.getName());
        reps.setText(currentExercise.getReps() + " reps");
        if(currentExercise.isRequireEquipment()){
            weight.setText(currentExercise.getWeight() + " kg");
        }
        else{
            weight.setText("Bodyweight exercise");
        }
        sets.setVisibility(View.VISIBLE);
        sets.setText("Sets: " + (currentExercise.getSets() - doneSets));
        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rest(index, doneSets);
            }
        });
        nextExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((index + 1) == schedule.lenght()){
                    Intent intent = new Intent(RunningWorkoutActivity.this, WorkoutTerminatedActivity.class);
                    RunningWorkoutActivity.this.startActivity(intent);
                    RunningWorkoutActivity.this.finish();
                }
                else{
                    startExercise(index + 1, 0);
                }
            }
        });
    }

    private void rest(int index, int doneSets){
        Exercise currentExercise = schedule.getExercises().get(index);
        doneSets++;
        if(doneSets == currentExercise.getSets()){
            // rest between sets
            sets.setVisibility(View.GONE);
            rest.setVisibility(View.VISIBLE);
            new CountDownTimer((currentExercise.getRest_between_exercises() + 1) * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    rest.setText("Exercise completed!\nRest: " + millisUntilFinished / 1000);
                }
                public void onFinish() {
                    if((index + 1) != schedule.lenght()){
                        rest.setText("Next exercise: " + schedule.getExercises().get(index + 1).getName());
                    }
                }
            }.start();
        }
        else{
            // rest between reps
            int doneSetsTmp = doneSets;
            sets.setVisibility(View.GONE);
            rest.setVisibility(View.VISIBLE);
            new CountDownTimer((currentExercise.getRest_between_sets() + 1) * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    rest.setText("Rest: " + millisUntilFinished / 1000);
                }
                public void onFinish() {
                    sets.setText("Sets: " + (currentExercise.getSets() - doneSetsTmp));
                    startExercise(index, doneSetsTmp);
                }
            }.start();
        }
    }
}