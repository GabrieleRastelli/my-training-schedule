package com.example.mytrainingschedules.activities.schedules;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopActivity extends AppCompatActivity {

    Button btn_save,btnUpS,btnDownS,btnUpR,btnDownR,btnUpW,btnDownW;
    private Animation scaleDown, scaleUp;
    String responseReturned;
    SeekBar restSeekBar;
    TextView timeToRest;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_exercise);

        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        restSeekBar=findViewById(R.id.appCompatSeekBar);
        timeToRest=findViewById(R.id.seconds);

        restSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        /* SET DIMENSIONS OF THE POPUP WINDOW */
        getWindow().setLayout((int)(width*.8), (int)(height*.8));


        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);

        String guid = getIntent().getStringExtra("guid");
        String url = getResources().getString(R.string.base_url) + "/exercise";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /* postLogin() function. */
        postExercise(getApplicationContext(), url, jsonObject);

        String test=responseReturned;

        btnUpS = findViewById(R.id.btn_up_s);
        btnUpS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btnUpS.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btnUpS.startAnimation(scaleUp);
                    TextView sets=(TextView) findViewById(R.id.set);
                    String numberSets=sets.getText().toString();
                    String newNumberSets = Integer.toString(Integer.parseInt(numberSets)+1);
                    sets.setText(newNumberSets);
                }
                return true;
            }
        });

        btnDownS = findViewById(R.id.btn_down_s);
        btnDownS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btnDownS.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btnDownS.startAnimation(scaleUp);
                    TextView sets=(TextView) findViewById(R.id.set);
                    String numberSets=sets.getText().toString();
                    String newNumberSets;
                    if(Integer.parseInt(numberSets)-1<0){
                        newNumberSets="0";
                    }else{
                        newNumberSets=Integer.toString(Integer.parseInt(numberSets)-1);
                    }
                    sets.setText(newNumberSets);
                }
                return true;
            }
        });

        btnUpR = findViewById(R.id.btn_up_r);
        btnUpR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btnUpR.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btnUpR.startAnimation(scaleUp);
                    TextView reps=(TextView) findViewById(R.id.rep);
                    String numberSets=reps.getText().toString();
                    String newNumberSets = Integer.toString(Integer.parseInt(numberSets)+1);
                    reps.setText(newNumberSets);
                }
                return true;
            }
        });

        btnDownR = findViewById(R.id.btn_down_r);
        btnDownR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btnDownR.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btnDownR.startAnimation(scaleUp);
                    TextView reps=(TextView) findViewById(R.id.rep);
                    String numberSets=reps.getText().toString();
                    String newNumberSets;
                    if(Integer.parseInt(numberSets)-1<0){
                        newNumberSets="0";
                    }else{
                        newNumberSets=Integer.toString(Integer.parseInt(numberSets)-1);
                    }
                    reps.setText(newNumberSets);
                }
                return true;
            }
        });

        btnUpW = findViewById(R.id.btn_up_w);
        btnUpW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btnUpW.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btnUpW.startAnimation(scaleUp);
                    TextView weight=(TextView) findViewById(R.id.peso);
                    String numberSets=weight.getText().toString();
                    String newNumberSets = Integer.toString(Integer.parseInt(numberSets)+1);
                    weight.setText(newNumberSets);
                }
                return true;
            }
        });

        btnDownW = findViewById(R.id.btn_down_w);
        btnDownW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btnDownW.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btnDownW.startAnimation(scaleUp);
                    TextView weight=(TextView) findViewById(R.id.peso);
                    String numberSets=weight.getText().toString();
                    String newNumberSets;
                    if(Integer.parseInt(numberSets)-1<0){
                        newNumberSets="0";
                    }else{
                        newNumberSets=Integer.toString(Integer.parseInt(numberSets)-1);
                    }
                    weight.setText(newNumberSets);
                }
                return true;
            }
        });

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btn_save.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btn_save.startAnimation(scaleUp);
                    finish();
                    restSeekBar.setProgress(0);
                }
                return true;
            }
        });
    }

    private void postExercise(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("APP_DEBUG", "Success: " + response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray result = jsonResponse.getJSONArray("result");
                    List<String> titoli= new ArrayList<String>();
                    for (int i = 0; i<result.length();i++){
                        if(result.getJSONObject(i).has("title")){
                            titoli.add(result.getJSONObject(i).getString("title"));
                        }
                    }
                    String[] TITLES = titoli.toArray(new String[0]);
                    AutoCompleteTextView editText = findViewById(R.id.title_ex);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, TITLES);
                    editText.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APP_DEBUG", "Fail: " + error.toString());
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }
}