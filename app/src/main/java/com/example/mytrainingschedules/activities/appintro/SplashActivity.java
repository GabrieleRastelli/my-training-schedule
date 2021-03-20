package com.example.mytrainingschedules.activities.appintro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    boolean firstAccess = false;
    TextView motivation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        //motivation = findViewById(R.id.motivation);
        //motivation.setText("\"Gucci Gang, Gucci Gang, Gucci Gang. Gucci Gang, Gucci Gang, Gucci Gang\"\nCit. - Lil Pump");

        if(fileExists(getApplicationContext(), "guid")){
            String guid = "";
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
            Log.d("APP_DEBUG", "The file doesn't exists, going to IntroActivity");
            firstAccess = true;
        }

        /* New Handler to start the LoginActivity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(firstAccess){
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
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
