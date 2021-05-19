package com.example.mytrainingschedules.activities.schedules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.example.mytrainingschedules.activities.utils.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopActivity extends AppCompatActivity {

    private TextView restTextView, repsTextView, setsTextView, weightTextView;
    private FloatingActionButton rep_add, rep_sub, set_add, set_sub, weight_add, weight_sub;
    private SeekBar seekBar;
    private String title, category;
    private int reps, sets, weight, rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_exercise);

        /*scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up); */

        seekBar = findViewById(R.id.seekBar);

        title = getIntent().getStringExtra("EXERCISE_TITLE");
        setTitle(title);

        /*restSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minutes = progress / 60;
                int seconds = progress - (minutes * 60);
                StringBuilder str = new StringBuilder();
                str.append(minutes);
                str.append(" Minutes ");
                str.append(seconds);
                str.append(" Seconds ");
                String restText=str.toString();
                timeToRest.setText(restText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/

        /*DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        /* SET DIMENSIONS OF THE POPUP WINDOW
        getWindow().setLayout((int)(width*.9), (int)(height*.9));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params); */

        initButtons();

        /*btn_save = findViewById(R.id.btn_save);
        btn_save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    btn_save.startAnimation(scaleDown);
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    btn_save.startAnimation(scaleUp);

                    TextView titleView = findViewById(R.id.title_ex);
                    String title = titleView.getText().toString();
                    Spinner exType = findViewById(R.id.ex_type);
                    String type = exType.getSelectedItem().toString();

                    TextView setView = findViewById(R.id.set);
                    int set = Integer.parseInt(setView.getText().toString());
                    TextView repView = findViewById(R.id.rep);
                    int rep = Integer.parseInt(repView.getText().toString());
                    TextView pesoView = findViewById(R.id.peso);
                    int peso = Integer.parseInt(pesoView.getText().toString());

                    String equipment = "FALSE";
                    SwitchCompat switchCompat = findViewById(R.id.switchCompat);
                    if(switchCompat.isChecked()){
                        equipment = "TRUE";
                    }

                    SeekBar restView = findViewById(R.id.appCompatSeekBar);
                    int secondsToRest = restView.getProgress();

                    int restBetweenExercises=0;

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("title", title);
                    resultIntent.putExtra("type", type);
                    resultIntent.putExtra("sets", set);
                    resultIntent.putExtra("reps", rep);
                    resultIntent.putExtra("peso", peso);
                    resultIntent.putExtra("equipment", equipment);
                    resultIntent.putExtra("rest-between-sets", secondsToRest);
                    resultIntent.putExtra("rest-between-exercises", restBetweenExercises);

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                    restSeekBar.setProgress(0);
                }
                return true;
            }
        });*/
    }

    private void initButtons(){
        rep_add = findViewById(R.id.rep_add);
        rep_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reps < 100){
                    reps++;
                    repsTextView.setText("Reps: " + reps);
                }
            }
        });
        rep_sub = findViewById(R.id.rep_sub);
        rep_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reps > 1){
                    reps--;
                    repsTextView.setText("Reps: " + reps);
                }
            }
        });
        set_add = findViewById(R.id.set_add);
        set_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sets < 30){
                    sets++;
                    setsTextView.setText("Sets: " + sets);
                }
            }
        });
        set_sub = findViewById(R.id.set_sub);
        set_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sets > 1){
                    sets--;
                    setsTextView.setText("Sets: " + sets);
                }
            }
        });
        weight_add = findViewById(R.id.weight_add);
        weight_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(weight < 300){
                    weight++;
                    weightTextView.setText("Weight: " + weight + " kg");
                }
            }
        });
        weight_sub = findViewById(R.id.weight_sub);
        weight_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(weight > 1){
                    weight--;
                    weightTextView.setText("Weight: " + weight + " kg");
                }
            }
        });
    }
}