package com.example.mytrainingschedules.activities.mainactivity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.Schedule;
import com.example.mytrainingschedules.activities.appintro.IntroActivity;
import com.example.mytrainingschedules.activities.appintro.SplashActivity;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    String guid;
    private HomeViewModel homeViewModel;
    ArrayList<Schedule> schedules;
    GridView gridView;
    CustomAdapter adapter;
    FloatingActionButton fab;
    TextView errorTextView;
    boolean connectionAvailable;
    JSONArray result = null;

    /*
    * getActivity() --> MainActivity
    * root          --> HomeFragment
    */

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        errorTextView = root.findViewById(R.id.errorTextView);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);
        connectionAvailable = false;

        /* FloatingActionButton listener: add a new schedule */
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionAvailable){
                    Toast.makeText(getActivity().getApplicationContext(), "FAB", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Unable to add schedule", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* transform GUID into JSONObject*/
        guid = getActivity().getIntent().getStringExtra("USER_GUID");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* get schedules of the user */
        getUserSchedules(getContext(), root, getResources().getString(R.string.base_url) + "/homeinfo", jsonObject);

        /* gridView OnClickListener*/
        gridView = root.findViewById(R.id.grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent(getContext(), ViewSchedule.class);
                    intent.putExtra("USER_GUID", guid);
                    intent.putExtra("SCHEDULE_ID", result.getJSONObject(i).getString("scheduleId"));
                    intent.putExtra("SCHEDULE_TITLE", result.getJSONObject(i).getString("title"));
                    intent.putExtra("SCHEDULE_DESCRIPTION", result.getJSONObject(i).getString("description"));
                    getContext().startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void getUserSchedules(Context context, View root, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                }

                adapter = new CustomAdapter(context, result);
                gridView.setAdapter(adapter);
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                connectionAvailable = false;
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