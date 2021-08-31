package com.example.mytrainingschedules.activities.applogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin, buttonRegister;
    private Animation scaleDown, scaleUp;
    private EditText email, password;
    private TextView emailError, passwordError, errorTextView;
    private ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        initGUI();

        buttonLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    buttonLogin.startAnimation(scaleDown);
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    buttonLogin.startAnimation(scaleUp);
                    email.clearFocus();
                    password.clearFocus();
                    errorTextView.setText("");

                    if(allFieldsCompiled()) {

                        String url = getResources().getString(R.string.base_url) + "/login";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("email", email.getText().toString());
                            jsonObject.put("password", password.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        postLogin(getApplicationContext(), url, jsonObject);

                    } else{
                        Toast.makeText(getApplicationContext(), getString(R.string.missing_fields), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        buttonRegister.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    buttonRegister.startAnimation(scaleDown);
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    buttonRegister.startAnimation(scaleUp);
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

    }

    private void postLogin(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);
        progressBar.setVisibility(View.VISIBLE);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.d("APP_DEBUG", "Success: " + response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONObject result = jsonResponse.getJSONObject("result");
                    String guid = result.getString("guid");
                    writeToFile("guid", guid, context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                String guid = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    guid = (String) jsonObject.getJSONObject("result").get("guid");
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }
                intent.putExtra("USER_GUID",guid);
                startActivity(intent);
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("APP_DEBUG", "Fail: " + error.toString());
                if(error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText(getString(R.string.cant_connect_server));
                }
                else if(error.toString().equals("com.android.volley.AuthFailureError")){
                    errorTextView.setText(getString(R.string.invalid_credentials));
                }
                else{
                    errorTextView.setText(getString(R.string.no_internet_connection));
                }
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }

    private void writeToFile(String filename, String data, Context context) {
        try {
            OutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            // failed to write the file
        }
    }

    private void initGUI(){
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.confirmPasswordEditText);


        emailError = findViewById(R.id.passwordError);
        passwordError = findViewById(R.id.confirmPasswordError);
        errorTextView = findViewById(R.id.errorTextView);

        buttonLogin = findViewById(R.id.button_login);
        buttonRegister = findViewById(R.id.button_register);
    }

    private boolean allFieldsCompiled(){
        boolean allFieldCompiled = true;
        if(email.getText().toString().equals("")) {
            emailError.setText("*");
            allFieldCompiled = false;
        }else{
            emailError.setText("");
        }
        if(password.getText().toString().equals("")) {
            passwordError.setText("*");
            allFieldCompiled = false;
        }else{
            passwordError.setText("");
        }
        return allFieldCompiled;
    }
}
