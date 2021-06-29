package com.example.mytrainingschedules.activities.mainactivity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.VolleyPostClient;
import com.example.mytrainingschedules.activities.mainactivity.user.UserPageActivity;
import com.example.mytrainingschedules.activities.mainactivity.user.UserPageActivity2;
import com.example.mytrainingschedules.activities.schedules.CreateScheduleActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * The HomeFragment is the home page of the app
 * Called by: SplashActivity, LoginActivity, RegisterActivity
 * Layout: home_fragment
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class HomeFragment extends Fragment {

    private String guid;
    private GridView gridView;
    private CustomAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private TextView errorTextView, numberOfSchedules;
    private ImageView errorImageView;
    private boolean connectionAvailable;
    private JSONArray result = null;
    private String imageB64;
    private ImageView immagineProfilo;
    private ProgressBar progressBar;

    /*
     * getActivity() --> MainActivity
     * root          --> HomeFragment
     */

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        errorImageView = root.findViewById(R.id.errorImageView);
        errorImageView.setVisibility(View.GONE);
        errorTextView = root.findViewById(R.id.errorTextView);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);
        connectionAvailable = false;

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        numberOfSchedules = root.findViewById(R.id.number_of_schedules);
        numberOfSchedules.setText("0 Schedules");

        /* Parse GUID into JSONObject. */
        guid = getActivity().getIntent().getStringExtra("USER_GUID");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* FloatingActionButton listener: add a new schedule. */
        floatingActionButton = getActivity().findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionAvailable){
                    Intent intent = new Intent(view.getContext(), CreateScheduleActivity.class);
                    intent.putExtra("USER_GUID", guid);
                    intent.putExtra("SCHEDULE", new Schedule());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Unable to add schedule", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* Get schedules of the user with getUserSchedules() function. */
        getUserSchedules(getContext(), jsonObject);

        /* Get user image with getUserImage() function */
        getUserImage(getContext(), jsonObject);

        /* User account page. */
        immagineProfilo = root.findViewById(R.id.user_home_image);
        immagineProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), UserPageActivity.class);
                Intent intent = new Intent(getActivity(), UserPageActivity2.class);
                intent.putExtra("USER_GUID", guid);
                intent.putExtra("N_SCHEDULES", numberOfSchedules.getText().toString());
                startActivity(intent);
            }
        });

        /* gridView OnClickListener. */
        gridView = root.findViewById(R.id.grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent(getContext(), ViewScheduleActivity.class);
                    intent.putExtra("USER_GUID", guid);
                    intent.putExtra("SCHEDULE_ID", result.getJSONObject(i).getInt("scheduleId"));
                    /* TODO: remove this after change api response */
                    intent.putExtra("SCHEDULE_TITLE", result.getJSONObject(i).getString("title"));
                    intent.putExtra("SCHEDULE_DESCRIPTION", result.getJSONObject(i).getString("description"));
                    //getActivity().finish();
                    getContext().startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void getUserSchedules(Context context, JSONObject jsonObject) {
        progressBar.setVisibility(View.VISIBLE);

        VolleyPostClient client = new VolleyPostClient(context, "/homeinfo", jsonObject);
        client.setProgressBar(progressBar);
        client.setErrorTextView(errorTextView);
        client.setErrorImageView(errorImageView);
        client.setDefaultErrorListener();

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                connectionAvailable = true;
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    result = jsonResponse.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(result.length() == 0){
                    errorTextView.setText("No schedules found, click the \"+\" button to add your first schedule!");
                    errorTextView.setTextColor(Color.DKGRAY);
                    errorTextView.setVisibility(View.VISIBLE);
                    numberOfSchedules.setText("" + 0);
                }

                numberOfSchedules.setText(result.length()+" Schedules");
                adapter = new CustomAdapter(context, result);
                gridView.setAdapter(adapter);
            }
        };
        client.setOnSuccessListener(onSuccessListener);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(client.getStringRequest());
    }

    private void getUserImage(Context context, JSONObject jsonObject) {
        VolleyPostClient client = new VolleyPostClient(context, "/userinfo", jsonObject);
        client.setErrorTextView(errorTextView);
        client.setErrorImageView(errorImageView);
        client.setDefaultErrorListener();

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                JSONObject result = null;
                imageB64 = null;
                try {
                    jsonResponse = new JSONObject(response);
                    result = jsonResponse.getJSONObject("result");
                    Iterator<?> keys = result.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        switch (key) {
                            case "profileImage":
                                imageB64 = result.get(key).toString();
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (imageB64 != null && !imageB64.isEmpty()) {

                    byte[] decodedString = Base64.decode(imageB64, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    immagineProfilo.setImageBitmap(decodedByte);
                }
            }
        };
        client.setOnSuccessListener(onSuccessListener);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(client.getStringRequest());
    }

}