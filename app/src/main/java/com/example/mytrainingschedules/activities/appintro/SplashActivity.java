package com.example.mytrainingschedules.activities.appintro;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private boolean firstAccess;
    private String guid;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        /* Setting firstAccess = false. This will decide,
        * after the SplashActivity, which activity start. */
        firstAccess = false;


        /* Control if the file "guid" is present.
        * If file "guid" is present, read the content and go to the
        * MainActivity (user already logged).
        * Else, start IntroActivity (settings firstAccess = true. */
        if(fileExists(getApplicationContext(), "guid")){
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
            Log.d("APP_DEBUG", "guid file doesn't exists, going to IntroActivity");
            firstAccess = true;
        }

        /* This block of code give the delay of SplashActivity*/
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
        return file != null && file.exists();
    }

    private void writeToFile(String filename, String data, Context context) {
        try {
            OutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            /* failed to write the file */
            e.printStackTrace();
        }
    }
}
