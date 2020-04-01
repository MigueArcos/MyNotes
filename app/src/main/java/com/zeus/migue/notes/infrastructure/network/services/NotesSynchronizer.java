package com.zeus.migue.notes.infrastructure.network.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.reflect.TypeToken;
import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.DTO.SyncRequest;
import com.zeus.migue.notes.data.DTO.SyncResponse;
import com.zeus.migue.notes.infrastructure.network.HttpClient;
import com.zeus.migue.notes.infrastructure.network.IResponseListener;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesSynchronizer implements INotesSynchronizer {
    private HttpClient httpClient;

    public NotesSynchronizer(Context context) {
        httpClient = HttpClient.getInstance(context);
    }

    @Override
    public void syncDatabases(SyncRequest syncRequest, IResponseListener<SyncResponse> successListener, IResponseListener<ErrorCode> errorListener) {
        StringRequest tRequest = new StringRequest(Request.Method.POST, HttpClient.URL + "/sync",
                response -> {
                    Log.d(Utils.GLOBAL_LOG_TAG, response);
                    SyncResponse remoteSyncDTO = SyncResponse.fromJson(response, SyncResponse.class);
                    //Log.d("response", response);
                    //MyTxtLogger.getInstance(AppContext).writeToSD("Response payload byte count: " + response.length() +" Bytes");
                    successListener.onResponse(remoteSyncDTO);
                },
                error -> {
                    // TODO Auto-generated method stub
                    errorListener.onResponse(httpClient.getVolleyError(error));
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                String payload = syncRequest.toJson();
                Log.d("Sync payload", payload);
                //Log.d("request", payload);
                //MyTxtLogger.getInstance(AppContext).writeToSD("Request payload byte count: " + payload.length() +" Bytes");
                return payload.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<String, String>() {{
                    /*put("is-first-time-sync", String.valueOf(isFirstTime));
                    put("refresh_token", refreshToken);
                    put("authorization", token);*/
                }};
            }
        };
        httpClient.addToRequestQueue(tRequest);
    }

    @Override
    public void getUserNotes(String token, IResponseListener<List<NoteDTO>> successListener, IResponseListener<ErrorCode> errorListener) {
        StringRequest tRequest = new StringRequest(Request.Method.GET, HttpClient.URL + "/notes",
                response -> {
                    Log.d(Utils.GLOBAL_LOG_TAG, response);
                    List<NoteDTO> remoteSyncDTO = HttpClient.JSON_SERIALIZER.fromJson(response, new TypeToken<List<NoteDTO>>() {
                    }.getType());
                    successListener.onResponse(remoteSyncDTO);
                },
                error -> {
                    // TODO Auto-generated method stub
                    errorListener.onResponse(httpClient.getVolleyError(error));
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<String, String>() {{
                    put("authorization", token);
                }};
            }
        };
        httpClient.addToRequestQueue(tRequest);
    }
}
