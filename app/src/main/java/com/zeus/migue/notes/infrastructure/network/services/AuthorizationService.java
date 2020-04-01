package com.zeus.migue.notes.infrastructure.network.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.RefreshTokenRequest;
import com.zeus.migue.notes.data.DTO.SignInRequest;
import com.zeus.migue.notes.data.DTO.SignInResponse;
import com.zeus.migue.notes.data.DTO.SignUpRequest;
import com.zeus.migue.notes.infrastructure.network.HttpClient;
import com.zeus.migue.notes.infrastructure.network.IResponseListener;

import java.nio.charset.StandardCharsets;

public class AuthorizationService implements IAuthorizationService {
    private HttpClient httpClient;
    public AuthorizationService(Context context){
        httpClient = HttpClient.getInstance(context);
    }

    @Override
    public void signIn(String email, String password, IResponseListener<SignInResponse> successListener, IResponseListener<ErrorCode> errorListener) {
        final SignInRequest signInDTO = new SignInRequest(email, password);
        StringRequest tRequest = new StringRequest(Request.Method.POST, HttpClient.URL + "/sign-in",
                response -> {
                    SignInResponse signInResponse = SignInResponse.fromJson(response, SignInResponse.class);
                    //Log.d("response", response);
                    successListener.onResponse(signInResponse);
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
                String payload = signInDTO.toJson();
                Log.d("Authorization payload", payload);
                //Log.d("request", payload);
                return payload.getBytes(StandardCharsets.UTF_8);
            }
        };
        httpClient.addToRequestQueue(tRequest);
    }

    @Override
    public void signUp(String email, String userName, String password, IResponseListener<SignInResponse> successListener, IResponseListener<ErrorCode> errorListener) {
        final SignUpRequest signUpDTO = new SignUpRequest(email, userName, password);
        StringRequest tRequest = new StringRequest(Request.Method.POST, HttpClient.URL + "/sign-up",
                response -> {
                    SignInResponse signInResponse = SignInResponse.fromJson(response, SignInResponse.class);
                    //Log.d("response", response);
                    successListener.onResponse(signInResponse);
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
                String payload = signUpDTO.toJson();
                Log.d("Authorization payload", payload);
                //Log.d("request", payload);
                return payload.getBytes(StandardCharsets.UTF_8);
            }
        };
        httpClient.addToRequestQueue(tRequest);
    }

    @Override
    public void refreshToken(String refreshToken, IResponseListener<SignInResponse> successListener, IResponseListener<ErrorCode> errorListener) {
        RefreshTokenRequest refreshTokenDTO = new RefreshTokenRequest(refreshToken);
        StringRequest tRequest = new StringRequest(Request.Method.POST, HttpClient.URL + "/refresh-token",
                response -> {
                    SignInResponse signInResponse = SignInResponse.fromJson(response, SignInResponse.class);
                    //Log.d("response", response);
                    successListener.onResponse(signInResponse);
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
                String payload = refreshTokenDTO.toJson();
                Log.d("RefreshToken payload", payload);
                //Log.d("request", payload);
                return payload.getBytes(StandardCharsets.UTF_8);
            }
        };
        httpClient.addToRequestQueue(tRequest);
    }
}
