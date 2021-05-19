package com.example.mytrainingschedules.activities.mainactivity.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.mytrainingschedules.activities.mainactivity.home.CustomAdapter;
import com.example.mytrainingschedules.activities.mainactivity.home.HomeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserPageActivity extends AppCompatActivity {

    JSONObject result = null;

    private HomeViewModel homeViewModel;

    private Animation scaleDown, scaleUp;
    private String email = null;
    private String name = null;
    private String guid = null;
    private String nickname = null;
    private String encodedImage = null;
    private ImageView editName,editNickname, userImage;
    private EditText changeName, changeNickname;
    public static final int PICK_IMAGE = 1;
    private TextView errorTextView, numberOfSchedules, username;
    private TextView nameView,emailView;
    private FloatingActionButton saveName, saveNickname;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page_layout);

        initGUI();

        /* transform GUID into JSONObject*/
        guid = this.getIntent().getStringExtra("USER_GUID");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getUserInfo(getApplicationContext(), findViewById(android.R.id.content).getRootView(), getResources().getString(R.string.base_url) + "/userinfo", jsonObject);

        numberOfSchedules.setText(getIntent().getStringExtra("N_SCHEDULES"));
    }

    private void initGUI(){
        errorTextView = findViewById(R.id.errorTextView2);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);

        saveName=findViewById(R.id.save_name);
        saveNickname=findViewById(R.id.save_nickname);
        emailView = findViewById(R.id.user_email);
        changeName=findViewById(R.id.change_name);
        changeNickname=findViewById(R.id.change_nickname);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        nameView = findViewById(R.id.user_name);

        username = findViewById(R.id.nickname);

        editName = findViewById(R.id.edit_name);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameView.setVisibility(View.INVISIBLE);
                editName.setVisibility(View.INVISIBLE);
                changeName.setText(name);
                changeName.setVisibility(View.VISIBLE);
                saveName.setVisibility(View.VISIBLE);
            }
        });

        editNickname = findViewById(R.id.edit_nickname);
        editNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setVisibility(View.INVISIBLE);
                editNickname.setVisibility(View.INVISIBLE);
                changeNickname.setText(nickname);
                changeNickname.setVisibility(View.VISIBLE);
                saveNickname.setVisibility(View.VISIBLE);
            }
        });

        userImage = findViewById(R.id.user_image);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName.setVisibility(View.INVISIBLE);
                saveName.setVisibility(View.INVISIBLE);
                name=changeName.getText().toString();
                nameView.setText(name);
                nameView.setVisibility(View.VISIBLE);
                editName.setVisibility(View.VISIBLE);

                /* update user profile */
                JSONObject jsonObject= new JSONObject();
                try {
                    jsonObject.put("guid",guid);
                    jsonObject.put("name",name);
                    jsonObject.put("email",email);
                    jsonObject.put("nickname",nickname);
                    jsonObject.put("image",encodedImage);
                }catch(JSONException e){
                    e.printStackTrace();
                }

                updateUser(getApplicationContext(), getResources().getString(R.string.base_url) + "/updateuser", jsonObject);
            }
        });

        saveNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNickname.setVisibility(View.INVISIBLE);
                saveNickname.setVisibility(View.INVISIBLE);
                nickname=changeNickname.getText().toString();
                username.setText(nickname);
                username.setVisibility(View.VISIBLE);
                editNickname.setVisibility(View.VISIBLE);
                /* update user profile */
                JSONObject jsonObject= new JSONObject();
                try {
                    jsonObject.put("guid",guid);
                    jsonObject.put("name",name);
                    jsonObject.put("email",email);
                    jsonObject.put("nickname",nickname);
                    jsonObject.put("image",encodedImage);
                }catch(JSONException e){
                    e.printStackTrace();
                }

                updateUser(getApplicationContext(), getResources().getString(R.string.base_url) + "/updateuser", jsonObject);
            }
        });
        numberOfSchedules=findViewById(R.id.schede_create_numero);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "Can't change image, try later.", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                byte[] bytes = IOUtils.toByteArray(is);
                encodedImage = Base64.encodeToString(bytes,Base64.DEFAULT);

                /* set image */
                Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                userImage.setImageBitmap(decodedByte);

                /* update user profile */
                JSONObject jsonObject= new JSONObject();
                try {
                    jsonObject.put("guid",guid);
                    jsonObject.put("name",name);
                    jsonObject.put("email",email);
                    jsonObject.put("nickname",nickname);
                    jsonObject.put("image",encodedImage);

                }catch(JSONException e){
                    e.printStackTrace();
                }

                updateUser(getApplicationContext(), getResources().getString(R.string.base_url) + "/updateuser", jsonObject);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Can't change image, try later.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUser(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //connectionAvailable = true;
                Toast.makeText(getApplicationContext(), "Succesfuly updated!", Toast.LENGTH_SHORT).show();
            }
        };

        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //connectionAvailable = false;
                Toast.makeText(getApplicationContext(), "Can't save information, try later.", Toast.LENGTH_SHORT).show();
                Log.d("APP_DEBUG", "Fail: " + error.toString());
                errorTextView.setVisibility(View.VISIBLE);
                if (error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText("Can't connect to the server");
                } else if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    errorTextView.setText("Invalid credentials");
                } else {
                    errorTextView.setText("No Internet connection");
                }
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }

    private void getUserInfo(Context context, View root, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;

                try {
                    jsonResponse = new JSONObject(response);
                    result = jsonResponse.getJSONObject("result");
                    Iterator<?> keys = result.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        switch (key) {
                            case "profileImage":
                                encodedImage = result.get(key).toString();
                                break;
                            case "email":
                                email = result.get(key).toString();
                                break;
                            case "name":
                                name = result.get(key).toString();
                                break;
                            case "nickname":
                                nickname = result.get(key).toString();
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                nameView.setText(name);
                emailView.setText(email);
                username.setText(nickname);
                if (encodedImage != null && !encodedImage.isEmpty() && !encodedImage.equals("null")) {
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    userImage.setImageBitmap(decodedByte);
                }
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APP_DEBUG", "Fail: " + error.toString());
                errorTextView.setVisibility(View.VISIBLE);
                if (error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText("Can't connect to the server");
                } else if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    errorTextView.setText("Invalid credentials");
                } else {
                    errorTextView.setText("No Internet connection");
                }
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }

}
