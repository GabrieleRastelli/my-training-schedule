package com.example.mytrainingschedules.activities.applogin;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.mytrainingschedules.activities.apphome.HomeActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class RegisterActivity extends AppCompatActivity {

    Button buttonRegister;
    EditText name, email, password, confirmPassword;
    TextView nameError, emailError, passwordError, confirmPasswordError;
    Animation scaleDown, scaleUp;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);

        nameError = findViewById(R.id.nameError);
        emailError = findViewById(R.id.passwordError);
        passwordError = findViewById(R.id.confirmPasswordError);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);

        buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    buttonRegister.startAnimation(scaleDown);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    buttonRegister.startAnimation(scaleUp);

                    nameError.setText("");
                    emailError.setText("");
                    passwordError.setText("");
                    confirmPasswordError.setText("");
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
                            writeToFile(data, getApplicationContext());
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);
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
}
