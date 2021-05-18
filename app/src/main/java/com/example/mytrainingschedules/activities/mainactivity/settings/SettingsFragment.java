package com.example.mytrainingschedules.activities.mainactivity.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mytrainingschedules.R;
import com.example.mytrainingschedules.activities.CustomStringRequest;
import com.example.mytrainingschedules.activities.mainactivity.home.CustomAdapter;
import com.example.mytrainingschedules.activities.mainactivity.home.ViewSchedule;
import com.example.mytrainingschedules.activities.schedules.CustomRecyclerViewAdapterExercises;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener {

    private SettingsViewModel settingsViewModel;
    List<Schedule> schede=new ArrayList<Schedule>();
    Context context;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    TextView errorTextView;
    private String guid;
    private boolean connectionAvailable;
    private JSONArray result = null;
    private EditText searchbar;
    private RecyclerViewAdapter adapter;
    private ArrayList<Schedule> filteredList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.settings_fragment, container, false);
        context=getActivity();

        filteredList=new ArrayList<>();
        errorTextView = root.findViewById(R.id.errorTextView);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);
        connectionAvailable = false;

        /* Parse GUID into JSONObject. */
        guid = getActivity().getIntent().getStringExtra("USER_GUID");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("guid", guid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView=root.findViewById(R.id.recycler_view);


        EditText searchbar=root.findViewById(R.id.searchbar);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        /* to set animation programmatically
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(animationController);*/

        /* Get all schedules. */
        getAllSchedules(getContext(), root, getResources().getString(R.string.base_url) + "/allschedules", jsonObject);

        /*RecyclerViewAdapter adapter=new RecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);*/



        return root;
    }

    private void filter(String restriction){
        filteredList=new ArrayList<Schedule>();
        for(Schedule schedule : schede){
            if(schedule.getTitle().toLowerCase().contains(restriction.toLowerCase()) || schedule.getCreator().toLowerCase().contains(restriction.toLowerCase())){
                filteredList.add(schedule);
            }
        }
        adapter.filterList(filteredList);
        recyclerView.scheduleLayoutAnimation(); /* without this method animation is not displayed */
    }

    /* to re-run animation */
    private void runLayoutAnimation(RecyclerView recyclerView){
        LayoutAnimationController layoutAnimationController=AnimationUtils.loadLayoutAnimation(context,R.anim.item_animation_fall_down);

        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void getAllSchedules(Context context, View root, String url, JSONObject jsonObject) {
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


                    if(result.length() == 0){
                        errorTextView.setText("No schedules found!");
                        errorTextView.setTextColor(Color.DKGRAY);
                        errorTextView.setVisibility(View.VISIBLE);
                    }

                    for (int i=0;i<result.length();i++){
                        JSONObject scheda = (JSONObject) result.get(i);
                        schede.add(new Schedule(scheda));
                    }

                    filteredList=new ArrayList<Schedule>(schede);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter=new RecyclerViewAdapter(context, schede, SettingsFragment.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scheduleLayoutAnimation();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), DownloadScheduleActivity.class);
        intent.putExtra("USER_GUID", guid);
        intent.putExtra("SCHEDULE_ID", String.valueOf(filteredList.get(position).getScheduleId()));
        intent.putExtra("SCHEDULE_TITLE", filteredList.get(position).getTitle());
        intent.putExtra("SCHEDULE_DESCRIPTION", filteredList.get(position).getDescription());
        getContext().startActivity(intent);
    }
}