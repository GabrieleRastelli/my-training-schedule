package com.example.mytrainingschedules.activities.apphome;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomAdapter;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout2);

        String[] data = {"uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove"};

        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), data);
        GridView gridView = findViewById(R.id.grid);
        gridView.setAdapter(adapter);



    }

}
