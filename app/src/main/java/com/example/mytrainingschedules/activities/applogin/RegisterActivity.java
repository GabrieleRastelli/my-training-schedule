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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonRegister;
    private EditText nickname, name, email, password, confirmPassword;
    private TextView nicknameError,nameError, emailError, passwordError, confirmPasswordError, errorTextView;
    private Animation scaleDown, scaleUp;
    private ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        /* Create this function to make code more readable. */
        initGUI();

        /* Register Button listener */
        buttonRegister.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    buttonRegister.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    buttonRegister.startAnimation(scaleUp);

                    nickname.clearFocus();
                    name.clearFocus();
                    email.clearFocus();
                    password.clearFocus();
                    confirmPassword.clearFocus();
                    nicknameError.setText("");
                    nameError.setText("");
                    emailError.setText("");
                    passwordError.setText("");
                    confirmPasswordError.setText("");
                    errorTextView.setText("");

                    if(allFieldCompiled()){

                        /* Check if passwords are equals. */
                        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                            passwordError.setText("*");
                            confirmPasswordError.setText("*");
                            Toast.makeText(getApplicationContext(), "Passwords are different", Toast.LENGTH_SHORT).show();
                        }else{

                            /* This is the POST request. */
                            String url = getResources().getString(R.string.base_url) + "/register";
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("name", email.getText().toString());
                                jsonObject.put("email", email.getText().toString());
                                jsonObject.put("password", password.getText().toString());
                                jsonObject.put("nickname", nickname.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            /* postRegister() function. */
                            postRegister(getApplicationContext(), url, jsonObject);

                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "Compile all fields to proceed with registration!", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            }
        });

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
                String guid = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    guid = (String) jsonObject.getJSONObject("result").get("guid");
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }
                /* saves guid in file */
                writeToFile("guid", guid, getApplicationContext());
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

    private void initGUI(){
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        nickname = findViewById(R.id.nicknameEditText);
        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);


        nicknameError = findViewById(R.id.nicknameError);
        nameError = findViewById(R.id.nameError);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);
        errorTextView = findViewById(R.id.errorTextView);

        buttonRegister = findViewById(R.id.button_register);
    }

    private boolean allFieldCompiled(){
        boolean allFieldCompiled = true;
        if(nickname.getText().toString().equals("")){
            nicknameError.setText("*");
            allFieldCompiled = false;
        }
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
        return allFieldCompiled;
    }
}
