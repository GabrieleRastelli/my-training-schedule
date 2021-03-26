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

public class RegisterActivity extends AppCompatActivity {

    Button buttonRegister;
    EditText name, email, password, confirmPassword;
    TextView nameError, emailError, passwordError, confirmPasswordError, errorTextView;
    Animation scaleDown, scaleUp;
    ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);

        //remove
        name.setText("Mattia");
        email.setText("mattiagualtieri@gmail.com");
        password.setText("password");
        confirmPassword.setText("password");

        nameError = findViewById(R.id.nameError);
        emailError = findViewById(R.id.passwordError);
        passwordError = findViewById(R.id.confirmPasswordError);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);
        errorTextView = findViewById(R.id.errorTextView);

        buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    buttonRegister.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    buttonRegister.startAnimation(scaleUp);

                    name.clearFocus();
                    email.clearFocus();
                    password.clearFocus();
                    confirmPassword.clearFocus();
                    nameError.setText("");
                    emailError.setText("");
                    passwordError.setText("");
                    confirmPasswordError.setText("");
                    errorTextView.setText("");
                    boolean allFieldCompiled = true;
                    if(name.getText().toString().equals("")){
                        nameError.setText("*");
                        allFieldCompiled = false;
                    }
                    if(email.getText().toString().equals("")){
                        emailError.setText("*");
                        allFieldCompiled = false;
                    }
                    if(password.getText().toString().equals("")){
                        passwordError.setText("*");
                        allFieldCompiled = false;
                    }
                    if(confirmPassword.getText().toString().equals("")){
                        confirmPasswordError.setText("*");
                        allFieldCompiled = false;
                    }

                    if(allFieldCompiled){

                        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                            passwordError.setText("*");
                            confirmPasswordError.setText("*");
                            Toast.makeText(getApplicationContext(), "Passwords are different", Toast.LENGTH_SHORT).show();
                        }else{
                            String data = name.getText().toString();

                            /* POST */
                            String url = "http://192.168.0.109:8080/register";
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("name", email.getText().toString());
                                jsonObject.put("email", email.getText().toString());
                                jsonObject.put("password", password.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            postRegister(getApplicationContext(), url, jsonObject);

                            //writeToFile(data, getApplicationContext());
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "Compile all fields to proceed with registration!", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            }
        });

    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStream outputStream = context.openFileOutput("config.csv", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            // failed to write the file
        }
    }

    private void postRegister(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);
        progressBar.setVisibility(View.VISIBLE);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.d("APP_DEBUG", "Success: " + response.toString());
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("APP_DEBUG", "Fail: " + error.toString());
                if (error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText("Can't connect to the server");
                } else if (error.toString().equals("com.android.volley.ServerError")) {
                    errorTextView.setText("User already registered");
                } else {
                    errorTextView.setText("No Internet connection");
                }
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }
}
