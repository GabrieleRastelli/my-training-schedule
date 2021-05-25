package com.example.mytrainingschedules.activities.schedules;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateScheduleActivity extends AppCompatActivity {

    FloatingActionButton btn_exe,save_schedule;

    private boolean connectionAvailable;

    RecyclerView exercisesView;
    RecyclerView.Adapter exercisesViewAdapter;
    LinearLayoutManager exercisesViewLayoutManager;

    private TextView errorTextView;

    List<JSONObject> exerciseDataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_schedule);

        errorTextView = findViewById(R.id.errorTextView);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);

        btn_exe=findViewById(R.id.add_exercise);
        save_schedule=findViewById(R.id.save_schedule);

        btn_exe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PopActivity.class);
                i.putExtra("guid",getIntent().getStringExtra("guid"));
                startActivityForResult(i, 0);

            }
        });

        save_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    JSONArray myArray = new JSONArray(exerciseDataSet);
                    JSONObject exercises= new JSONObject();
                    exercises.put("exercises",myArray);
                    String dataJson=exercises.toString();

                    Intent i = new Intent(getApplicationContext(), CompleteScheduleActivity.class);
                    i.putExtra("guid",getIntent().getStringExtra("guid"));
                    i.putExtra("dataJson",dataJson);
                    startActivity(i);

                }catch(JSONException e){
                    Toast.makeText(getApplicationContext(), "Unable to save schedule", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* dataset init
        for (int i=0;i<20;i++){
            exerciseDataSet.add(new ListItem( Integer.toString(i), "petto",3,8, 30, "FALSE", 90));
        }*/

        exercisesViewLayoutManager= new LinearLayoutManager(this);

        exercisesViewAdapter = new CustomAdapterExercise(exerciseDataSet,this);

        exercisesView=(RecyclerView) findViewById(R.id.setsRecyclerView);
        exercisesView.setHasFixedSize(true);
        exercisesView.setLayoutManager(exercisesViewLayoutManager);
        exercisesView.setAdapter(exercisesViewAdapter);


        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(exercisesView.getContext(), exercisesViewLayoutManager.getOrientation());
        dividerItemDecoration.setOrientation(DividerItemDecoration.HORIZONTAL);
        exercisesView.addItemDecoration(dividerItemDecoration);*/

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper. RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged=dragged.getAdapterPosition();
                int position_target=target.getAdapterPosition();

                Collections.swap(exerciseDataSet, position_dragged, position_target);

                exercisesViewAdapter.notifyItemMoved(position_dragged, position_target);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position=viewHolder.getAdapterPosition();
                exerciseDataSet.remove(position);
                exercisesViewAdapter.notifyItemRemoved(position);
            }
        });

        helper.attachToRecyclerView(exercisesView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == Activity.RESULT_OK) {

                    JSONObject exercise=new JSONObject();
                    try {
                        exercise.put("exercise-name", data.getStringExtra("title"));
                        exercise.put("sets", data.getIntExtra("sets",0));
                        exercise.put("reps", data.getIntExtra("reps",0));
                        exercise.put("weight", data.getIntExtra("peso",0));
                        exercise.put("rest-between-sets", data.getIntExtra("rest-between-sets",0));
                        exercise.put("rest-between-exercises", data.getIntExtra("rest-between-exercises",0));
                        exercise.put("type", data.getStringExtra("type"));
                        exercise.put("equipment", data.getStringExtra("equipment"));
                        exerciseDataSet.add(exercise);
                        exercisesViewAdapter.notifyDataSetChanged();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }


                    // TODO Update your TextView.
                }
                break;
            }
        }
    }


}
