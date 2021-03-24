package com.example.mytrainingschedules.activities.appintro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    boolean firstAccess;
    String guid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        firstAccess = false;

        if(fileExists(getApplicationContext(), "guid")){
            /* file "guid" is present, read the content and go to the MainActivity */
            guid = "";
            try {
                FileInputStream fileInputStream = getApplicationContext().openFileInput("guid");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                guid = bufferedReader.readLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("APP_DEBUG", "GUID: " + guid);
        }
        else{
            /* file "guid" doesn't exists, go to the IntroActivity */
            Log.d("APP_DEBUG", "guid file doesn't exists, going to IntroActivity");
            firstAccess = true;
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent;
                if(firstAccess){
                    intent = new Intent(SplashActivity.this, IntroActivity.class);
                }
                else{
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("USER_GUID", guid);
                }
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    public boolean fileExists(Context context, String filename){
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()){
            return false;
        }
        return true;
    }

}
