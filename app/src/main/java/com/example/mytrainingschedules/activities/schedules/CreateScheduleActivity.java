package com.example.mytrainingschedules.activities.schedules;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.mainactivity.home.CustomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CreateScheduleActivity extends AppCompatActivity {

    FloatingActionButton btn_exe;

    RecyclerView exercisesView;
    RecyclerView.Adapter exercisesViewAdapter;
    RecyclerView.LayoutManager exercisesViewLayoutManager;

    List<ListItem> exerciseDataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_schedule);

        btn_exe=findViewById(R.id.add_exercise);

        btn_exe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PopActivity.class);
                i.putExtra("guid",getIntent().getStringExtra("guid"));
                startActivity(i);
            }
        });

        for (int i=0;i<20;i++){
            exerciseDataSet.add(new ListItem( Integer.toString(i), "descr"));
        }

        exercisesViewLayoutManager= new LinearLayoutManager(this);

        exercisesViewAdapter = new CustomAdapterExercise(exerciseDataSet,this);

        exercisesView=(RecyclerView) findViewById(R.id.exercise_view);
        exercisesView.setHasFixedSize(true);
        exercisesView.setLayoutManager(exercisesViewLayoutManager);
        exercisesView.setAdapter(exercisesViewAdapter);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        exercisesView.addItemDecoration(divider);


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
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

            }
        });

        helper.attachToRecyclerView(exercisesView);
    }
}
