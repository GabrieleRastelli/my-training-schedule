package com.example.mytrainingschedules.activities.applogin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;

public class LoginActivity extends AppCompatActivity {

    Button buttonLogin, buttonRegister;
    Animation scaleDown, scaleUp;
    EditText email, password;
    TextView emailError, passwordError;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.confirmPasswordEditText);

        emailError = findViewById(R.id.passwordError);
        passwordError = findViewById(R.id.confirmPasswordError);

        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    buttonLogin.startAnimation(scaleDown);
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    buttonLogin.startAnimation(scaleUp);

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

                    if(allFieldCompiled) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Compile all fields to proceed with login!", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    buttonRegister.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    buttonRegister.startAnimation(scaleUp);
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

    }
}
