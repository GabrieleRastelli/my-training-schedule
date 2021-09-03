package com.pomegranatesoftware.mytrainingschedules.activities.mainactivity.explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pomegranatesoftware.mytrainingschedules.R;
import com.pomegranatesoftware.mytrainingschedules.activities.CustomStringRequest;
import com.pomegranatesoftware.mytrainingschedules.activities.mainactivity.search.DownloadScheduleActivity;
import com.pomegranatesoftware.mytrainingschedules.activities.mainactivity.search.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is the one who handles the "POPULAR" part of explore schedules
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class PopularFragment extends Fragment implements SuggestedAdapter.OnItemClickListener{

    private String TAG="PopularFragment";
    private RecyclerView recyclerView;
    private String guid;
    private boolean connectionAvailable=true;
    private JSONArray result = null;
    private ArrayList<Schedule> filteredList;
    private List<Schedule> schede;
    private SuggestedAdapter adapter;
    private List <String> categoriesToDisplay;

    /**
     * Constructor
     * @param categoriesToDisplay
     */
    public PopularFragment(List<String> categoriesToDisplay){
        this.categoriesToDisplay=new ArrayList<>();
        this.categoriesToDisplay=categoriesToDisplay;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_popular, container, false);

        recyclerView=root.findViewById(R.id.schedule_popular_recycler);

        /* gets guid from intent */
        guid = getActivity().getIntent().getStringExtra("USER_GUID");

        try {
            Log.i(TAG, "Calling callPopularSchedules()");
            callPopularSchedules(root);
        } catch (JSONException je) {
            Log.e(TAG, "An error occurred while preparing popularschedules request body", je);
            Toast.makeText(getActivity(), getString(R.string.error_popular_schedules), Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    /**
     * This method is the that filters schedules to display basing on the categories received in constructor.
     * Every time user selects a category fragment is recreated.
     */
    private void filter(){
        filteredList=new ArrayList<>();

        for(Schedule schedule : schede){
            if(categoriesToDisplay.size()==0 || categoriesToDisplay.contains(schedule.getCategoria1().toUpperCase()) || categoriesToDisplay.contains(schedule.getCategoria2().toUpperCase())){
                filteredList.add(schedule);
            }
        }
        adapter.filterList(filteredList);
        //recyclerView.scheduleLayoutAnimation(); /* without this method animation is not displayed */
    }

    /**
     * This method prepares body to call popularschedules endpoint
     * @param root
     * @throws JSONException
     */
    private void callPopularSchedules(View root) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("guid", guid);

        getPopularSchedules(getContext(), root, getResources().getString(R.string.base_url) + "/popularschedules", jsonObject);
    }

    private void getPopularSchedules(Context context, View root, String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> onSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                connectionAvailable = true;
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    result = jsonResponse.getJSONArray("result");

                    schede=new ArrayList<Schedule>();
                    for (int i=0;i<result.length();i++){
                        JSONObject scheda = (JSONObject) result.get(i);
                        schede.add(new Schedule(scheda));
                    }

                    filteredList=new ArrayList<Schedule>(schede);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter =new SuggestedAdapter(context, schede, PopularFragment.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scheduleLayoutAnimation();
                    /* filters schedules */
                    filter();
                } catch (JSONException e) {
                    Log.e(TAG,"An error occurred while parsing popular schedule returned from server", e);
                    Toast.makeText(getActivity(), getString(R.string.error_popular_schedules), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };

        Response.ErrorListener onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                connectionAvailable = false;
                Log.e(TAG, "Fail in calling popularschedules endpoint: ", error);
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
        Intent intent = new Intent(getContext(), DownloadScheduleActivity.class);
        intent.putExtra("USER_GUID", guid);
        intent.putExtra("SCHEDULE_ID", String.valueOf(filteredList.get(position).getScheduleId()));
        intent.putExtra("SCHEDULE_TITLE", filteredList.get(position).getTitle());
        intent.putExtra("SCHEDULE_DESCRIPTION", filteredList.get(position).getDescription());
        getContext().startActivity(intent);
    }
}