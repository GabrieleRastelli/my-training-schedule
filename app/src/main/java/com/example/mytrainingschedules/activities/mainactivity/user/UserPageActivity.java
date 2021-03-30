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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserPageActivity extends AppCompatActivity {

    JSONObject result = null;

    private HomeViewModel homeViewModel;

    String email = null;
    String name = null;
    String imageB64 = null;
    String guid = null;

    TextView errorTextView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page_layout);

        errorTextView = findViewById(R.id.errorTextView2);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);

        /* transform GUID into JSONObject*/
        guid = this.getIntent().getStringExtra("USER_GUID");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getUserInfo(getApplicationContext(), findViewById(android.R.id.content).getRootView(), getResources().getString(R.string.base_url) + "/userinfo", jsonObject);

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
                            case "image":
                                imageB64 = result.get(key).toString();
                                break;
                            case "email":
                                email = result.get(key).toString();
                                break;
                            case "name":
                                name = result.get(key).toString();
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TextView nameView = findViewById(R.id.user_name);
                nameView.setText(name);
                TextView emailView = findViewById(R.id.user_email);
                emailView.setText(email);
                if (imageB64 != null) {
                    ImageView profileImageView = findViewById(R.id.user_image);

                    byte[] decodedString = Base64.decode(imageB64, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profileImageView.setImageBitmap(decodedByte);
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
