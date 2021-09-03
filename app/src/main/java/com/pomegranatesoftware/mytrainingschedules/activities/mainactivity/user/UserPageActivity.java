package com.pomegranatesoftware.mytrainingschedules.activities.mainactivity.user;

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
import com.pomegranatesoftware.mytrainingschedules.R;
import com.pomegranatesoftware.mytrainingschedules.activities.CustomStringRequest;
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
public class UserPageActivity extends AppCompatActivity {

    private JSONObject result = null;

    private String TAG="UserPageActivity";
    public static final int PICK_IMAGE = 1;
    private Animation scaleDown, scaleUp;
    private String email = null;
    private String name = null;
    private String guid = null;
    private String nickname = null;
    private String created = null;
    private String download = null;
    private String encodedImage = null;
    private ImageView editName, editNickname, userImage;
    private EditText changeName, changeNickname;
    private TextView errorTextView, createdSchedules, downloaded, username;
    private TextView nameView,emailView;
    private FloatingActionButton saveName, saveNickname;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page_layout);

        /* gets GUID from intent */
        guid = this.getIntent().getStringExtra("USER_GUID");
        Log.i(TAG, "USER_GUID:"+guid);

        /* initialize GUI objects */
        initGui();

        /* sets User information */
        try {
            Log.i(TAG, "Calling callGetUserInfo()");
            callGetUserInfo();
        } catch (JSONException je) {
            Log.e(TAG, "An error occurred while preparing getuserinfo request body", je);
            Toast.makeText(getApplicationContext(), getString(R.string.error_user_info), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method that instantiate GUI objects
     */
    private void initGui(){

        Log.i(TAG, "Starting GUI init");

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

        createdSchedules=findViewById(R.id.schede_create_numero);
        downloaded=findViewById(R.id.schede_scaricate_numero);

        /* makes editText appear when modifying name field */
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User clicked on editName button");
                nameView.setVisibility(View.INVISIBLE);
                editName.setVisibility(View.INVISIBLE);
                changeName.setText(name);
                changeName.setVisibility(View.VISIBLE);
                saveName.setVisibility(View.VISIBLE);
            }
        });

        /* makes editText appear when modifying nickname field */
        editNickname = findViewById(R.id.edit_nickname);
        editNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User clicked on editName nickname");
                username.setVisibility(View.INVISIBLE);
                editNickname.setVisibility(View.INVISIBLE);
                changeNickname.setText(nickname);
                changeNickname.setVisibility(View.VISIBLE);
                saveNickname.setVisibility(View.VISIBLE);
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

        /* makes api call to save new user information when clicking on save button */
        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeName.setVisibility(View.INVISIBLE);
                saveName.setVisibility(View.INVISIBLE);
                name=changeName.getText().toString();
                nameView.setText(name);
                nameView.setVisibility(View.VISIBLE);
                editName.setVisibility(View.VISIBLE);

                Log.i(TAG, "User clicked on saveName button, calling updateUser");
                try {
                    /* saves changes */
                    callUpdateUser();
                } catch (JSONException je) {
                    Log.e(TAG, "An error occurred while preparing updateuser request body", je);
                    Toast.makeText(getApplicationContext(), getString(R.string.error_save_name), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* makes api call to save new user information when clicking on save button */
        saveNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User clicked on saveNickname button");
                changeNickname.setVisibility(View.INVISIBLE);
                saveNickname.setVisibility(View.INVISIBLE);
                nickname=changeNickname.getText().toString();
                username.setText(nickname);
                username.setVisibility(View.VISIBLE);
                editNickname.setVisibility(View.VISIBLE);

                boolean nicknameValid = true;
                /* checking wheter nickname is valid or not */
                String nick = nickname.trim();
                for(int i = 0; i < nick.length(); i++){
                    if((nick.charAt(i) >= 58 && nick.charAt(i) <= 94) || (nick.charAt(i) >= 33 && nick.charAt(i) <= 47) || nick.charAt(i) >= 123){
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
                        Toast.makeText(getApplicationContext(), getString(R.string.error_save_nickname), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.e(TAG, "User nickname: "+nickname+" is not valid");
                    Toast.makeText(getApplicationContext(), getString(R.string.error_wrong_nick_format), Toast.LENGTH_SHORT).show();
                }

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
                Toast.makeText(getApplicationContext(), getString(R.string.error_change_image), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), getString(R.string.error_change_image), Toast.LENGTH_SHORT).show();
            } catch (JSONException je){
                Log.e(TAG, "An error occurred while preparing updateuser request body", je);
                Toast.makeText(getApplicationContext(), getString(R.string.error_change_image), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), getString(R.string.success_update), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Successfully called updateUser");
            }
        };

        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //connectionAvailable = false;
                Toast.makeText(getApplicationContext(), getString(R.string.error_save_info), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Fail in calling updateUser: " ,error);
                errorTextView.setVisibility(View.VISIBLE);
                if (error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText(getString(R.string.cant_connect_server));
                } else if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    errorTextView.setText(getString(R.string.invalid_credentials));
                } else {
                    errorTextView.setText(getString(R.string.no_internet_connection));
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


                nameView.setText(name);

                emailView.setText(email);
                TextView nicknameView = findViewById(R.id.nickname);
                nicknameView.setText(nickname);
                username.setText(nickname);
                createdSchedules.setText(created);
                downloaded.setText(download);

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
