package com.pomegranatesoftware.mytrainingschedules.activities.mainactivity.search;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pomegranatesoftware.mytrainingschedules.R;
import com.pomegranatesoftware.mytrainingschedules.activities.CustomStringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is the one who handles searching of schedules
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class SearchFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener {

    private String TAG="SearchFragment";
    private List<Schedule> schede=new ArrayList<Schedule>();
    private Context context;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private TextView errorTextView;
    private String guid;
    private boolean connectionAvailable;
    private JSONArray result = null;
    private EditText searchBar;
    private RecyclerViewAdapter adapter;
    private ArrayList<Schedule> filteredList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_fragment, container, false);
        context=getActivity();
        connectionAvailable = false;

        initGui(root);

        /* list containing filtered schedules */
        filteredList=new ArrayList<>();

        /* gets GUID from intent. */
        guid = getActivity().getIntent().getStringExtra("USER_GUID");
        Log.i(TAG, "USER_GUID:"+guid);

        try {
            Log.i(TAG, "Calling callGetAllScheudules()");
            callGetAllScheudules(root);
        } catch (JSONException je) {
            Log.e(TAG, "An error occurred while preparing getallschedules request body", je);
            Toast.makeText(context, getString(R.string.error_get_schedules), Toast.LENGTH_SHORT).show();
        }

        return root;
    }


    /**
     * Method that instantiate GUI objects
     * @param root
     */
    private void initGui(View root){

        Log.i(TAG, "Starting GUI init");

        floatingActionButton = getActivity().findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.GONE);

        errorTextView = root.findViewById(R.id.errorTextView);
        errorTextView.setText("");
        errorTextView.setVisibility(View.GONE);

        recyclerView=root.findViewById(R.id.recycler_view);

        /* This is the searchbar that permits to filter schedules */
        searchBar=root.findViewById(R.id.searchbar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    /**
     * Method that prepares request body and calls allschedules endpoint
     * @param root
     * @throws JSONException
     */
    private void callGetAllScheudules(View root) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("guid", guid);

        /* Get all schedules. */
        getAllSchedules(getContext(), root, getResources().getString(R.string.base_url) + "/allschedules", jsonObject);
    }

    /**
     * Method that filters schedules basing on the given String restriction
     * @param restriction
     */
    private void filter(String restriction){
        filteredList=new ArrayList<>();

        for(Schedule schedule : schede){
            /* filtering is done on both schedule title and creator */
            if(schedule.getTitle().toLowerCase().contains(restriction.toLowerCase()) || schedule.getCreator().toLowerCase().contains(restriction.toLowerCase())){
                filteredList.add(schedule);
            }
        }
        adapter.filterList(filteredList);
        //recyclerView.scheduleLayoutAnimation(); /* without this method animation is not displayed when filtering list*/
    }

    /**
     * Method that retrieves all schedules in DB
     * @param context
     * @param root
     * @param url
     * @param jsonObject
     */
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
                        errorTextView.setText(getString(R.string.no_schedules));
                        errorTextView.setTextColor(Color.DKGRAY);
                        errorTextView.setVisibility(View.VISIBLE);
                    }

                    for (int i=0;i<result.length();i++){
                        JSONObject scheda = (JSONObject) result.get(i);
                        schede.add(new Schedule(scheda));
                    }

                    filteredList=new ArrayList<Schedule>(schede);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter=new RecyclerViewAdapter(context, schede, SearchFragment.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scheduleLayoutAnimation();

                } catch (JSONException je) {
                    Log.e(TAG, "An error occurred while calling allschedules", je);
                    Toast.makeText(context, getString(R.string.error_get_schedule_info), Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "Successfully called allschedules endpoint");
            }
        };

        /* onErrorListener */
        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                connectionAvailable = false;
                Log.e(TAG, "Fail in calling allschedules endpoint: ", error);
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
     * This method is triggered when one schedule is clicked. Starts DownloadScheduleActivity
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Log.i(TAG, "User clicked on filtered schedule no. "+position+" starting DownloadScheduleActivity");
        Intent intent = new Intent(getContext(), DownloadScheduleActivity.class);
        intent.putExtra("USER_GUID", guid);
        intent.putExtra("SCHEDULE_ID", String.valueOf(filteredList.get(position).getScheduleId()));
        intent.putExtra("SCHEDULE_TITLE", filteredList.get(position).getTitle());
        intent.putExtra("SCHEDULE_DESCRIPTION", filteredList.get(position).getDescription());
        getContext().startActivity(intent);
    }
}