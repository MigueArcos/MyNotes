package com.zeus.migue.notes.infrastructure.network.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.reflect.TypeToken;
import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.DTO.sync.SyncPayload;
import com.zeus.migue.notes.infrastructure.network.HttpClient;
import com.zeus.migue.notes.infrastructure.network.IResponseListener;
import com.zeus.migue.notes.infrastructure.services.contracts.IDatabaseSynchronizer;
import com.zeus.migue.notes.infrastructure.services.implementations.DatabaseSynchronizer;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Synchronizer implements ISynchronizer {
    private HttpClient httpClient;
    private IDatabaseSynchronizer databaseSynchronizer;
    public Synchronizer(Context context) {
        httpClient = HttpClient.getInstance(context);
        databaseSynchronizer = new DatabaseSynchronizer(context);
    }

    @Override
    public void syncDatabases(String token, String refreshToken, String lastSyncDate, IResponseListener<SyncPayload> successListener, IResponseListener<ErrorCode> errorListener) {
        SyncPayload syncRequest = databaseSynchronizer.buildLocalPayload(lastSyncDate);
        if (syncRequest == null) {
            errorListener.onResponse(new ErrorCode(0, "", true));
            return;
        }
        StringRequest tRequest = new StringRequest(Request.Method.POST, HttpClient.URL + "/sync",
                response -> {
                    Log.d(Utils.GLOBAL_LOG_TAG, response);
                    SyncPayload syncResponse = Utils.fromJson(response, SyncPayload.class, true);
                    boolean databaseStatus = databaseSynchronizer.synchronize(syncResponse);
                    //Log.d("response", response);
                    //MyTxtLogger.getInstance(AppContext).writeToSD("Response payload byte count: " + response.length() +" Bytes");
                    if (databaseStatus) successListener.onResponse(syncResponse);
                    else errorListener.onResponse(new ErrorCode(0, "", true));
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
                String payload = syncRequest.toJson(true);
                Log.d("Sync payload", payload);
                //Log.d("request", payload);
                //MyTxtLogger.getInstance(AppContext).writeToSD("Request payload byte count: " + payload.length() +" Bytes");
                return payload.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders(){
                return new HashMap<String, String>() {{
                    if (!Utils.stringIsNullOrEmpty(refreshToken)) put("refresh_token", refreshToken);
                    put("authorization", token);
                }};
            }
        };
        httpClient.addToRequestQueue(tRequest);
    }
}
