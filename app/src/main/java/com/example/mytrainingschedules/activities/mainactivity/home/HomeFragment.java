package com.example.mytrainingschedules.activities.mainactivity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.applogin.LoginActivity;
import com.example.mytrainingschedules.activities.mainactivity.CustomAdapter;
import com.example.mytrainingschedules.activities.mainactivity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    GridView gridView;
    CustomAdapter adapter;
    FloatingActionButton fab;
    TextView errorTextView;
    ImageView badImageView;

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
        badImageView = root.findViewById(R.id.badImageView);
        badImageView.setVisibility(View.GONE);

        JSONObject jsonObject = new JSONObject();
        Log.d("APP_DEBUG", "GUID in Home: " + getActivity().getIntent().getStringExtra("USER_GUID"));
        try {
            jsonObject.put("guid", getActivity().getIntent().getStringExtra("USER_GUID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getUserSchedules(getContext(), getResources().getString(R.string.base_url) + "/homeinfo", jsonObject);

        String[] data = {"uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove"};
        adapter = new CustomAdapter(this.getContext(), data);

        /* maybe this "homeViewModel" is completely useless, for the moment we keep it */
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                fab = getActivity().findViewById(R.id.fab);
                fab.setVisibility(View.VISIBLE);
                gridView = root.findViewById(R.id.grid);
                gridView.setAdapter(adapter);
            }
        });

        return root;
    }

    private void getUserSchedules(Context context, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("APP_DEBUG", "Success: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APP_DEBUG", "Fail: " + error.toString());
                errorTextView.setVisibility(View.VISIBLE);
                badImageView.setVisibility(View.VISIBLE);
                if(error.toString().equals("com.android.volley.TimeoutError")) {
                    errorTextView.setText("Can't connect to the server");
                }
                else if(error.toString().equals("com.android.volley.AuthFailureError")){
                    errorTextView.setText("Invalid credentials");
                }
                else{
                    errorTextView.setText("No Internet connection");
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    try {
                        params.put(key, jsonObject.getString(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
}