package com.example.mytrainingschedules.activities.schedules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.Exercise;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class EditExerciseActivity extends AppCompatActivity implements RecyclerViewClickListener {

    /*
     * EditExerciseActivity: used to edit an exercise (sets and rest)
     * Called by: EditScheduleActivity
     * Layout: edit_exercise_layout
     */

    private RecyclerView setsRecyclerView;
    private EditExerciseRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String title, category, guid;
    private int rest;
    private Exercise exercise;
    private ArrayList<Set> sets;
    private TextView startingMessage, titleTextView, restTextView;
    private FloatingActionButton save;
    private NumberPicker numberPicker;
    private Button addset;
    private Schedule schedule;
    private int scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* TODO: far si che questa activity sia un PopUp */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_exercise_layout);

        title = getIntent().getStringExtra("EXERCISE_TITLE");
        scheduleId = getIntent().getIntExtra("SCHEDULE_ID", -1);
        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        ArrayList<Exercise> exercises = schedule.getExercises();
        int index = getIntent().getIntExtra("INDEX", -1);

        exercise = exercises.get(index);
        sets = exercise.getSets();

        setsRecyclerView = findViewById(R.id.setsRecyclerView);
        setsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        setsRecyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new EditExerciseRecyclerViewAdapter(sets, this);
        setsRecyclerView.setAdapter(recyclerViewAdapter);

        /* drag and drop items in recycler view */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int positionDragged = dragged.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                Collections.swap(sets, positionDragged, positionTarget);
                recyclerViewAdapter.notifyItemMoved(positionDragged, positionTarget);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                sets.remove(position);
                recyclerViewAdapter.notifyItemRemoved(position);
            }
        });
        helper.attachToRecyclerView(setsRecyclerView);

        initUI();

    }

    private void initUI(){
        titleTextView = findViewById(R.id.activityTitle);
        titleTextView = findViewById(R.id.activityTitle);
        titleTextView.setText(title);

        startingMessage = findViewById(R.id.msg);
        if(sets.size() == 0){
            startingMessage.setVisibility(View.VISIBLE);
        }
        else{
            startingMessage.setVisibility(View.GONE);
        }

        rest = exercise.getRest();
        numberPicker = findViewById(R.id.numberPicker);
        String[] seconds = new String[600/5];
        for(int i = 0; i < seconds.length; i++){
            seconds[i] = Integer.toString(i * 5 + 5);
        }
        numberPicker.setDisplayedValues(seconds);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(seconds.length - 1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(rest/5);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldV, int newV) {
                rest = newV * 5 + 5;
            }
        });

        addset = findViewById(R.id.add);
        addset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startingMessage.setVisibility(View.GONE);
                sets.add(new Set(recyclerViewAdapter.getLastRepCount(), recyclerViewAdapter.getLastWeightCount()));
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sets.size() == 0){
                    Toast.makeText(getApplicationContext(), "Add at least one set", Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean repsnull = false;
                    for (Set set: sets) {
                        if(set.getReps() == 0){
                            repsnull = true;
                        }
                    }
                    if(repsnull){
                        Toast.makeText(getApplicationContext(), "There is a set with 0 reps!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        exercise.setSets(sets);
                        exercise.setRest(rest);
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
                    }
                }
            }
        });

    }

    @Override
    public void recyclerViewListClicked(View view, int position) {
        // nothing
    }

}