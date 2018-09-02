package com.example.miguel.misnotas;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miguel.misnotas.models.SyncData;
import com.example.miguel.misnotas.models.UserInfo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context AppContext;
    //private final String URL = "http://miguelarcos.x10.mx/android/movil";
    private final String URL = "http://miguelsnotes.azurewebsites.net";
    private final int SYNC_TIME = 7200000;

    public interface NotesResponseListener {
        void onSyncSuccess(SyncData.SyncInfo syncInfo);

        void onSyncError(String error);
    }

    public interface LoginListener {
        void onLoginSuccess(int id_usuario, String username, String email, int syncTime);

        void onLoginSuccess(UserInfo userInfo, int syncTime);

        void onLoginError(String error);

        void activateAutoSync(int time);
    }


    private VolleySingleton(Context context) {
        AppContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // LoginActivity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(AppContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void syncDatabases(final SyncData localSyncData, final NotesResponseListener listener) {
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL + "/DatabaseSync.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.charAt(0) != '{') {
                            listener.onSyncError(getVolleyError(null));
                            return;
                        }
                        SyncData remoteSyncData = new Gson().fromJson(response, SyncData.class);
                        SyncData.SyncInfo syncInfo = Database.getInstance(AppContext).updateLocalDatabase(localSyncData, remoteSyncData);
                        String localJson = new Gson().toJson(localSyncData);
                        //Log.d(MyUtils.GLOBAL_LOG_TAG, "JSON Local" + localJson);
                        //Log.d(MyUtils.GLOBAL_LOG_TAG, "JSON Remote" + response);
                        listener.onSyncSuccess(syncInfo);
                        MyTxtLogger.getInstance().writeToSD("Written " + (response.length() + localJson.length()) + " Bytes");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        listener.onSyncError(getVolleyError(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String syncDataJson = new Gson().toJson(localSyncData);
                params.put("syncDataJson", syncDataJson);
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }


    public void syncAzureDatabases(final SyncData localSyncData, final NotesResponseListener listener) {
        StringRequest MyRequest = new StringRequest(Request.Method.POST,
                URL + "/Notes/SyncDatabases",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.charAt(0) != '{') {
                            listener.onSyncError(getVolleyError(null));
                            return;
                        }
                        String localJson = new Gson().toJson(localSyncData);
                        Log.d(MyUtils.GLOBAL_LOG_TAG, "Local payload : " + localJson);
                        Log.d(MyUtils.GLOBAL_LOG_TAG, "Remote payload : " + response);
                        SyncData remoteSyncData = new Gson().fromJson(response, SyncData.class);
                        if (remoteSyncData.getSyncInfo().getLastSyncedId() != localSyncData.getSyncInfo().getLastSyncedId()){
                            MyTxtLogger.getInstance().writeToSD("Local payload : \n\n\n" + localJson);
                            MyTxtLogger.getInstance().writeToSD("Remote payload : \n\n\n" + response);
                        }
                        SyncData.SyncInfo syncInfo = Database.getInstance(AppContext).updateLocalDatabase(localSyncData, remoteSyncData);


                        listener.onSyncSuccess(syncInfo);
                        MyTxtLogger.getInstance().writeToSD("Written " + (response.length() + localJson.length()) + " Bytes");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        listener.onSyncError(getVolleyError(error));
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String payload = localSyncData.toJson();
                Log.d("Sync payload", payload);
                try {
                    //Log.d("request", payload);
                    return payload == null ? null : payload.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", payload, "utf-8");
                    return null;
                }

            }
        };
        addToRequestQueue(MyRequest);
    }


    public void IniciarSesion(final String email, final String password, final LoginListener loginListener) {
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL + "/IniciarSesion.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("No encontrado")) {
                            try {
                                JSONObject respuesta = new JSONObject(response);
                                loginListener.onLoginSuccess(respuesta.getInt("id_usuario"), respuesta.getString("username"), respuesta.getString("email"), SYNC_TIME);
                                Log.d("Pruebas", response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loginListener.onLoginError("Datos de usuario no encontrados");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        loginListener.onLoginError(getVolleyError(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }


    public void Registrar(final String username, final String email, final String password, final LoginListener loginListener) {
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL + "/Registrar.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("Email repetido")) {
                            try {
                                JSONObject respuesta = new JSONObject(response);
                                loginListener.onLoginSuccess(respuesta.getInt("id_usuario"), respuesta.getString("username"), respuesta.getString("email"), SYNC_TIME);
                                //return ;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loginListener.onLoginError("Este correo electrónico ya ha sido registrado");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        loginListener.onLoginError(getVolleyError(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("username", username);
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }

    public void Login(final UserInfo userInfo, final LoginListener loginListener, final boolean isSignUp) {
        String endPoint = isSignUp ? "SignUp" : "SignIn";
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL + "/api/Login/" + endPoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserInfo allUserInfo = new Gson().fromJson(response, UserInfo.class);
                        //Log.d("response", response);
                        loginListener.onLoginSuccess(allUserInfo, SYNC_TIME);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        loginListener.onLoginError(getVolleyError(error));
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String payload = userInfo.toJson();
                Log.d("Payload Login", payload);
                try {
                    //Log.d("request", mRequestBody);
                    return payload == null ? null : payload.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", payload, "utf-8");
                    return null;
                }

            }
        };
        addToRequestQueue(MyRequest);
    }


    private String getVolleyError(VolleyError error) {
        String message = "Unknown error";
        if (error == null){
            return message;
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
            message = "Error desconocido";
        }
        if (error.networkResponse != null) {
            try {
                message = new String(error.networkResponse.data, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return message;
    }


}