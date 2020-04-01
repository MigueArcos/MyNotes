package com.zeus.migue.notes.infrastructure.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zeus.migue.notes.data.DTO.ErrorCode;

import java.nio.charset.StandardCharsets;

public class HttpClient {
    private static HttpClient mInstance;
    private RequestQueue mRequestQueue;
    private static Context AppContext;
    public static final String URL = "https://mynotes-731ba.firebaseapp.com/api/v1";
    public static final int SYNC_TIME = 7200000;
    public static final Gson JSON_SERIALIZER = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private final DefaultRetryPolicy requestPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    private HttpClient(Context context) {
        AppContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized HttpClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HttpClient(context.getApplicationContext());
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // LoginActivity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(AppContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(requestPolicy);
        getRequestQueue().add(req);
    }

    public ErrorCode getVolleyError(VolleyError error) {
        String message = "Unknown error";
        try {
            if (error == null) {
                return new ErrorCode(4000, message, true);
            }
            if (error.networkResponse != null) {
                return ErrorCode.fromJson(new String(error.networkResponse.data, StandardCharsets.UTF_8), ErrorCode.class);
            }
            if (error instanceof NetworkError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (error instanceof ServerError) {
                message = "The server could not be found. Please try again after some time!!";
            } else if (error instanceof AuthFailureError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (error instanceof ParseError) {
                message = "Parsing error! Please try again after some time!!";
            } else if (error instanceof TimeoutError) {
                message = "Connection TimeOut! Please check your internet connection.";
            } else {
                message = "Unknown error";
            }
            return new ErrorCode(4000, message, true);
        } catch (Exception e) {
            //Log.e(Utils.GLOBAL_LOG_TAG, e.getMessage());
            return new ErrorCode(4000, message, true);
        }
    }
}
