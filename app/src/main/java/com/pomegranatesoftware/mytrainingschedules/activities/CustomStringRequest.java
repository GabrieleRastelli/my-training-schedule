package com.pomegranatesoftware.mytrainingschedules.activities;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// TODO: delete this class, and replace with VolleyPostClient

public class CustomStringRequest extends StringRequest {

    JSONObject jsonObject;

    public CustomStringRequest(int method, String url, JSONObject jsonObject, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.jsonObject = jsonObject;
    }

    @Override
    protected Map<String,String> getParams(){
        Map<String,String> params = new HashMap<String, String>();
        for (Iterator<String> it = this.jsonObject.keys(); it.hasNext(); ) {
            String key = it.next();
            try {
                params.put(key, this.jsonObject.getString(key));
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
}
