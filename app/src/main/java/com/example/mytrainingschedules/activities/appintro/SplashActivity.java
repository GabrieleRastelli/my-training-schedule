package com.example.mytrainingschedules.activities.appintro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.apphome.HomeActivity;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    boolean firstAccess = false;
    TextView motivation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        motivation = findViewById(R.id.motivation);
        motivation.setText("\"Gucci Gang, Gucci Gang, Gucci Gang. Gucci Gang, Gucci Gang, Gucci Gang\"\nCit. - Lil Pump");

        if(fileExists(getApplicationContext(), "config.csv")){
            Log.d("APP_DEBUG", "The file exists, going to LoginActivity");
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
                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
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
