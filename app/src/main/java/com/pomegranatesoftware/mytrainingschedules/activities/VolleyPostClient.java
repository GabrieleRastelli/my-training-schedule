package com.pomegranatesoftware.mytrainingschedules.activities;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pomegranatesoftware.mytrainingschedules.R;

import org.json.JSONObject;

/**
 * Class that implements a simple POST client, using Volley
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class VolleyPostClient {

    private boolean connectionAvailable;
    private Context context;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private ImageView errorImageView;
    private Response.Listener<String> onSuccessListener;
    private Response.ErrorListener onErrorListener;
    private String url;
    private JSONObject jsonObject;
    private static final String BASE_URL = "http://65.21.49.16:8080";
    private static final String TIMEOUT_ERROR = "com.android.volley.TimeoutError";
    private static final String AUTH_FAILURE_ERROR = "com.android.volley.AuthFailureError";

    public VolleyPostClient(Context context, String url, JSONObject jsonObject){
        this.context = context;
        this.url = BASE_URL + url;
        this.jsonObject = jsonObject;
        progressBar = null;
        errorTextView = null;
    }

    public void setOnSuccessListener(Response.Listener<String> listener){
        this.onSuccessListener = listener;
    }

    public void setOnErrorListener(Response.ErrorListener listener){
        this.onErrorListener = listener;
    }

    public void setErrorTextView(TextView errorTextView){
        this.errorTextView = errorTextView;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setErrorImageView(ImageView errorImageView) {
        this.errorImageView = errorImageView;
    }

    public void setDefaultErrorListener(){
        onErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                connectionAvailable = false;
                if (progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
                if (errorTextView != null){
                    errorTextView.setVisibility(View.VISIBLE);
                    if (error.toString().equals(TIMEOUT_ERROR)) {
                        errorTextView.setText(context.getResources().getString(R.string.error_));
                    } else if (error.toString().equals(AUTH_FAILURE_ERROR)) {
                        errorTextView.setText(context.getResources().getString(R.string.user_already_registered));
                    } else {
                        errorTextView.setText(context.getResources().getString(R.string.no_internet_connection));
                        if (errorImageView != null){
                            errorImageView.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else {
                    Toast.makeText(context, context.getResources().getString(R.string.cant_connect_server), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public CustomStringRequest getStringRequest(){
        return new CustomStringRequest(Request.Method.POST, url, jsonObject, onSuccessListener, onErrorListener);
    }

}
