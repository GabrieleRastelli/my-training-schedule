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
    boolean firstAccess = false, accessToServer = true;
    String name;
    String email;
    TextView errorTextView;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        firstAccess = false;
        accessToServer = true;
        name = "";
        email = "";
        errorTextView = findViewById(R.id.errorTextView);
        errorTextView.setText("");
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if(fileExists(getApplicationContext(), "guid")){
            /* file "guid" is present, read the content and get user info */
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

            /* POST */
            String url = "http://192.168.0.109:8080/userinfo";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("guid", guid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getUserInfo(getApplicationContext(), url, jsonObject);
        }
        else{
            Log.d("APP_DEBUG", "guid file doesn't exists, going to IntroActivity");
            firstAccess = true;
        }

        /* New Handler to start the LoginActivity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(firstAccess){
                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }
                else if(!firstAccess && accessToServer){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
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

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setAccessToServer(boolean access){
        this.accessToServer = access;
    }

    private void getUserInfo(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("APP_DEBUG", "Success: " + response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONObject result = jsonResponse.getJSONObject("result");
                    setName(result.getString("name"));
                    setEmail(result.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("APP_DEBUG", "Fail: " + error.toString());
                setAccessToServer(false);
                if(error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText("Can't connect to the server");
                }
                else if(error.toString().equals("com.android.volley.AuthFailureError")){
                    errorTextView.setText("Invalid credentials");
                }
                else{
                    errorTextView.setText("No Internet connection");
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    try {
                        params.put(key, jsonObject.getString(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

}
