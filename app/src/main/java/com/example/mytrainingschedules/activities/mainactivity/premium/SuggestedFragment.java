package com.example.mytrainingschedules.activities.mainactivity.premium;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.mainactivity.settings.RecyclerViewAdapter;
import com.example.mytrainingschedules.activities.mainactivity.settings.Schedule;
import com.example.mytrainingschedules.activities.mainactivity.settings.SettingsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SuggestedFragment extends Fragment {

    private RecyclerView recyclerView;
    private String guid;
    private boolean connectionAvailable=true;
    private JSONArray result = null;
    private ArrayList<Schedule> filteredList;
    private List<Schedule> schede=new ArrayList<Schedule>();
    private SuggestedAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_suggested, container, false);


        recyclerView=root.findViewById(R.id.schedule_suggested_recycler);

        guid = getActivity().getIntent().getStringExtra("USER_GUID");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getUserSchedules(getContext(), root, getResources().getString(R.string.base_url) + "/suggestedinfo", jsonObject);


        return root;
    }

    private void getUserSchedules(Context context, View root, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                connectionAvailable = true;
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    result = jsonResponse.getJSONArray("result");

                    for (int i=0;i<result.length();i++){
                        JSONObject scheda = (JSONObject) result.get(i);
                        schede.add(new Schedule(scheda));
                    }

                    filteredList=new ArrayList<Schedule>(schede);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter =new SuggestedAdapter(context, schede);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scheduleLayoutAnimation();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                connectionAvailable = false;
                Log.d("APP_DEBUG", "Fail: " + error.toString());
            }
        };

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);

        queue.add(stringRequest);
    }
}