package com.example.mytrainingschedules.activities.schedules;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytrainingschedules.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateScheduleActivity extends AppCompatActivity {

    FloatingActionButton btn_exe;
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
    }
}
