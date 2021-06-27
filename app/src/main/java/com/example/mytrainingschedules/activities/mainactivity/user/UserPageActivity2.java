package com.example.mytrainingschedules.activities.mainactivity.user;

import android.util.Log;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * This activity is the one that handles user page. It's invoked from HomeFragment
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class UserPageActivity2 extends AppCompatActivity {

    private JSONObject result = null;

    private String TAG = "UserPageActivity";
    public static final int PICK_IMAGE = 1;
    private Animation scaleDown, scaleUp;
    private String email = null;
    private String name = null;
    private String guid = null;
    private String nickname = null;
    private String created = null;
    private String download = null;
    private String encodedImage = null;
    private TextView editName, editNickname;
    private boolean saveName, saveNickname;
    private ImageView userImage;
    private EditText nicknameEditText, nameEditText, emailEditText;
    private TextView errorTextView, createdSchedules, downloadedSchedules;
    private TextView nameView, emailView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page_layout2);

        /* gets GUID from intent */
        guid = this.getIntent().getStringExtra("USER_GUID");
        Log.i(TAG, "USER_GUID: " + guid);

        /* initialize GUI objects */
        initGui();

        /* sets User information */
        try {
            Log.i(TAG, "Calling callGetUserInfo()");
            callGetUserInfo();
        } catch (JSONException je) {
            Log.e(TAG, "An error occurred while preparing getuserinfo request body", je);
            Toast.makeText(getApplicationContext(), "Can't get user information, try later.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method that instantiate GUI objects
     */
    private void initGui(){

        Log.i(TAG, "Starting GUI init");

        errorTextView = findViewById(R.id.errorTextView);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);
        editNickname = findViewById(R.id.edit_nickname);
        editName = findViewById(R.id.edit_name);
        emailEditText = findViewById(R.id.email);
        emailEditText.setText("");
        nameEditText = findViewById(R.id.name);
        nameEditText.setText("");
        nicknameEditText = findViewById(R.id.nickname);
        nicknameEditText.setText("");
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        createdSchedules = findViewById(R.id.schede_create_numero);
        downloadedSchedules = findViewById(R.id.schede_scaricate_numero);
        saveName = false;
        saveNickname = false;

        /* makes editText appear when modifying name field */
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User clicked on editName button");
                name = nameEditText.getText().toString().trim();
                if(saveName){
                    try {
                        /* saves changes */
                        callUpdateUser();
                    } catch (JSONException je) {
                        Log.e(TAG, "An error occurred while preparing updateuser request body", je);
                        Toast.makeText(getApplicationContext(), "Can't save name, try later.", Toast.LENGTH_SHORT).show();
                    }
                    nameEditText.setEnabled(false);
                    editName.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.edit_icon_black));
                    saveName = false;
                }
                else{
                    nameEditText.setEnabled(true);
                    editName.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.check_green));
                    saveName = true;
                }
            }
        });

        /* makes editText appear when modifying nickname field */
        editNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User clicked on editName nickname");
                nickname = nicknameEditText.getText().toString().trim();
                if(saveNickname){
                    boolean nicknameValid = true;
                    /* checking wheter nickname is valid or not */
                    for(int i = 0; i < nickname.length(); i++){
                        if((nickname.charAt(i) >= 58 && nickname.charAt(i) <= 94) || (nickname.charAt(i) >= 33 && nickname.charAt(i) <= 47) || nickname.charAt(i) >= 123){
                            nicknameValid = false;
                        }
                    }
                    if(nicknameValid){
                        Log.i(TAG, "User nickname: "+nickname+" is valid, calling updateUser");
                        try {
                            /* saves changes */
                            callUpdateUser();
                        } catch (JSONException je) {
                            Log.e(TAG, "An error occurred while preparing updateuser request body", je);
                            Toast.makeText(getApplicationContext(), "Can't save nickname, try later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Log.e(TAG, "User nickname: "+nickname+" is not valid");
                        Toast.makeText(getApplicationContext(), "Username can't contain special characters and needs to be lower", Toast.LENGTH_SHORT).show();
                    }
                    nicknameEditText.setEnabled(false);
                    editNickname.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.edit_icon_black));
                    saveNickname = false;
                }
                else{
                    nicknameEditText.setEnabled(true);
                    editNickname.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.check_green));
                    saveNickname = true;
                }
            }
        });

        userImage = findViewById(R.id.user_image);
        /* starts intent to get image when clicking on usr profile image */
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User clicked on image. Starting intent to get new profile image");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

    }


    /**
     * Method used to get user chosen image
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (data == null) {
                Log.i(TAG, "User didn't chose a valid image, not calling updateUser");
                Toast.makeText(getApplicationContext(), "Can't change image, try later.", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                byte[] bytes = IOUtils.toByteArray(is);
                /* base64 encodes user image */
                encodedImage = Base64.encodeToString(bytes,Base64.DEFAULT);

                /* set image */
                Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                userImage.setImageBitmap(decodedByte);

                Log.i(TAG, "User chose new profile image, calling updateUser");

                /* saves changes */
                callUpdateUser();
            } catch (IOException e) {
                Log.e(TAG, "An error occurred while reading image", e);
                Toast.makeText(getApplicationContext(), "Can't change image, try later.", Toast.LENGTH_SHORT).show();
            } catch (JSONException je){
                Log.e(TAG, "An error occurred while preparing updateuser request body", je);
                Toast.makeText(getApplicationContext(), "Can't change image, try later.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Method that prepares request body and calls updateuser endpoint
     * @throws JSONException
     */
    private void callUpdateUser() throws JSONException {

        /* update user profile */
        JSONObject jsonObject= new JSONObject();

        jsonObject.put("guid",guid);
        jsonObject.put("name",name);
        jsonObject.put("email",email);
        jsonObject.put("nickname",nickname);
        jsonObject.put("image",encodedImage);

        updateUser(getApplicationContext(), getResources().getString(R.string.base_url) + "/updateuser", jsonObject);
    }


    private void updateUser(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //connectionAvailable = true;
                Toast.makeText(getApplicationContext(), "Succesfuly updated!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Successfully called updateUser");
            }
        };

        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //connectionAvailable = false;
                Toast.makeText(getApplicationContext(), "Can't save information, try later.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Fail in calling updateUser: " ,error);
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


    /**
     * Method that prepares request body and calls getuserinfo endpoint
     * @throws JSONException
     */
    private void callGetUserInfo() throws JSONException {
        /* transform GUID into JSONObject*/
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("guid", guid);

        /* call to collect user information to display */
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
                            case "created":
                                created = result.get(key).toString();
                                if (created.equals("null")){
                                    created="0";
                                }
                                break;
                            case "download":
                                download = result.get(key).toString();
                                if (download.equals("null")){
                                    download="0";
                                }
                                break;
                        }
                    }
                } catch (JSONException je) {
                    Log.e(TAG, "An error occurred while calling getuserinfo", je);
                    Toast.makeText(getApplicationContext(), "Can't get user information, try later.", Toast.LENGTH_SHORT).show();
                    return;
                }

                nameEditText.setText(name);
                emailEditText.setText(email);
                nicknameEditText.setText(nickname);
                createdSchedules.setText(created);
                downloadedSchedules.setText(download);

                if (encodedImage != null && !encodedImage.isEmpty() && !encodedImage.equals("null")) {
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    userImage.setImageBitmap(decodedByte);
                }

                Log.i(TAG, "Successfully got userInfo");
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Fail in calling getuserinfo: " ,error);
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
