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

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String imageB64 = null;
    private String guid = null;
    private String nickname = null;
    ImageView editName,editNickname, userImage;
    private Button buttonEdit;
    public static final int PICK_IMAGE = 1;
    TextView errorTextView, numberOfSchedules, username;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page_layout);

        initGUI();

        numberOfSchedules.setText(getIntent().getStringExtra("N_SCHEDULES"));
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

    private void initGUI(){
        errorTextView = findViewById(R.id.errorTextView2);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);

        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);


        username = findViewById(R.id.nickname);

        editName = findViewById(R.id.edit_name);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "vuoi cambiare nome", Toast.LENGTH_SHORT).show();
            }
        });

        editNickname = findViewById(R.id.edit_nickname);
        editNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "vuoi cambiare nickname", Toast.LENGTH_SHORT).show();
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
                /*InputStream inputStream = getContentResolver().openInputStream(data.getData());
                IOUtils
                byte[] imageBytes = new byte[(int)file.length()];
                inputStream.read(imageBytes, 0, imageBytes.length);
                inputStream.close();
                String imageStr = org.apache.commons.codec.binary.Base64.encodeBase64String(imageBytes);*/
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Can't change image, try later.", Toast.LENGTH_SHORT).show();
            }
        }
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
                            case "nickname":
                                nickname = result.get(key).toString();
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
                TextView nicknameView = findViewById(R.id.nickname);
                nicknameView.setText(nickname);
                if (imageB64 != null && !imageB64.isEmpty()) {
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
