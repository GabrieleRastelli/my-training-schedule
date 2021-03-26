package com.example.mytrainingschedules.activities.mainactivity.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ArrayList<Schedule> schedules;
    GridView gridView;
    CustomAdapter adapter;
    FloatingActionButton fab;
    TextView errorTextView;

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

        /* FloatingActionButton listener: add a new schedule */
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "FAB", Toast.LENGTH_SHORT).show();
            }
        });

        /* transform GUID into JSONObject*/
        JSONObject jsonObject = new JSONObject();
        Log.d("APP_DEBUG", "GUID in Home: " + getActivity().getIntent().getStringExtra("USER_GUID"));
        try {
            jsonObject.put("guid", getActivity().getIntent().getStringExtra("USER_GUID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* get schedules of the user */
        getUserSchedules(getContext(), root, getResources().getString(R.string.base_url) + "/homeinfo", jsonObject);

        return root;
    }

    private void getUserSchedules(Context context, View root, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        /* onSuccessListener */
        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("APP_DEBUG", "Success: " + response.toString());

                String[] data = {"uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove"};
                adapter = new CustomAdapter(context, data);
                gridView = root.findViewById(R.id.grid);
                gridView.setAdapter(adapter);
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