package com.pomegranatesoftware.mytrainingschedules.activities.schedules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pomegranatesoftware.mytrainingschedules.R;
import com.pomegranatesoftware.mytrainingschedules.activities.CustomAlertDialog;
import com.pomegranatesoftware.mytrainingschedules.activities.Exercise;
import com.pomegranatesoftware.mytrainingschedules.activities.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

/**
 * CreateScheduleActivity: used to create a new schedule
 * Called by: HomeFragment
 * Layout: create_schedule_layout
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class CreateScheduleActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private RecyclerView listOfExercises;
    private EditScheduleRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Schedule schedule;
    private String guid, title, description;
    private int scheduleId = 0; // SEMPRE 0!
    private ArrayList<Exercise> exercises;
    FloatingActionButton add, next;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_schedule_layout);

        guid = getIntent().getStringExtra("USER_GUID");
        schedule = (Schedule) getIntent().getSerializableExtra("SCHEDULE");
        title = "";
        description = "";
        exercises = schedule.getExercises();

        message = findViewById(R.id.message);
        if(exercises.size() == 0){
            message.setVisibility(View.VISIBLE);
        }
        else{
            message.setVisibility(View.GONE);
        }

        listOfExercises = findViewById(R.id.setsRecyclerView);
        listOfExercises.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listOfExercises.setLayoutManager(layoutManager);
        recyclerViewAdapter = new EditScheduleRecyclerViewAdapter(exercises, this, this);
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

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exercises.size() == 0){
                    Toast.makeText(getApplicationContext(), getString(R.string.one_ex_at_least), Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean setnull = false;
                    for (Exercise exercise: exercises) {
                        if(exercise.getSetsNumber() == 0){
                            setnull = true;
                        }
                    }
                    if(setnull){
                        Toast.makeText(getApplicationContext(), getString(R.string.ex_0_sets), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), SetScheduleDataActivity.class);
                        intent.putExtra("SCHEDULE", schedule);
                        intent.putExtra("USER_GUID", guid);
                        startActivityForResult(intent, 0);
                    }
                }
            }
        });

    }

    @Override
    public void recyclerViewListClicked(View view, int position) {
        Exercise currentExercise = exercises.get(position);
        Intent intent = new Intent(getApplicationContext(), EditExerciseActivity.class);
        intent.putExtra("EXERCISE_TITLE", currentExercise.getName());
        intent.putExtra("SCHEDULE", schedule);
        intent.putExtra("SCHEDULE_ID", scheduleId);
        intent.putExtra("USER_GUID", guid);
        intent.putExtra("INDEX", position);
        startActivityForResult(intent, 0);
        recyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        if(exercises.size() == 0){
            CreateScheduleActivity.this.finish();
        }
        else{
            CustomAlertDialog alertDialog = new CustomAlertDialog(CreateScheduleActivity.this, getString(R.string.exit), getString(R.string.exit_confirm));
            alertDialog.setListenerPositive(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CreateScheduleActivity.this.finish();
                }
            });
            runOnUiThread(alertDialog);
        }
    }

}