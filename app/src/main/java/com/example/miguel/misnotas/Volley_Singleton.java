package com.example.miguel.misnotas;

import android.content.Context;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Volley_Singleton {

    private static Volley_Singleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context AppContext;
    private final String URL="http://miguelarcos.x10.mx/android/movil";
    private final int SYNC_TIME=3600000;
    public interface NotesResponseListener{
        void onSyncSuccess(int UltimoIDSync, int TotalNumberOfNotes);
        void onSyncError(String error);
    }
    public interface LoginListener{
        void onLoginSuccess(int id_usuario, String username, String email, int sync_time);
        void onLoginError(String error);
        void activateAutoSync(int time);
    }
    public interface audioUploadListener{
        void onFileUploadSuccess(String serverMessage);
        void onFileUploadError(String serverMessage);
    }
    private Volley_Singleton(Context AppContext) {
        this.AppContext = AppContext;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Volley_Singleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Volley_Singleton(context.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(AppContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void syncDBLocal_Remota(final String NotasSync, final String NotasNoSync, final int id_usuario, final int UltimoIDSync, final boolean isLogin, final NotesResponseListener listener){
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL+"/OperacionesBD.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        JSONArray array= null;
                        JSONObject SyncData= null;
                        try {
                            array=new JSONArray(response);
                            SyncData = array.getJSONObject(array.length()-1);
                            Database.getInstance(AppContext).NotasServidorALocalDB(array,isLogin);
                            listener.onSyncSuccess(SyncData.getInt("UltimoIDSync"), SyncData.getInt("TotalNumberOfNotes"));
                            Log.d("JSON",response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        listener.onSyncError(getVolleyError(error));
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                if (!NotasNoSync.equals("")){
                    params.put("NotasNoSyncJSON", NotasNoSync);
                }
                if (!NotasSync.equals("")){
                    params.put("NotasSyncJSON", NotasSync);
                }
                params.put("id_usuario",String.valueOf(id_usuario));
                params.put("UltimoIDSync", String.valueOf(UltimoIDSync));
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }
    public void IniciarSesion(final String email, final String password, final LoginListener loginListener){
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL+"/IniciarSesion.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("No encontrado")){
                            try {
                                JSONObject respuesta=new JSONObject(response);
                                loginListener.onLoginSuccess(respuesta.getInt("id_usuario"),respuesta.getString("username"),respuesta.getString("email"),SYNC_TIME);
                                Log.d("Pruebas", response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            loginListener.onLoginError("Datos de usuario no encontrados");
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        loginListener.onLoginError(getVolleyError(error));
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }
    public void Registrar(final String username, final String email, final String password, final LoginListener loginListener){
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL+"/Registrar.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("Email repetido")){
                            try {
                                JSONObject respuesta=new JSONObject(response);
                                loginListener.onLoginSuccess(respuesta.getInt("id_usuario"),respuesta.getString("username"),respuesta.getString("email"),SYNC_TIME);
                                //return ;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            loginListener.onLoginError("Este correo electrónico ya ha sido registrado");
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        loginListener.onLoginError(getVolleyError(error));
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);
                params.put("username",username);
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }

    private String getVolleyError(VolleyError error){
        String message = "Unknown error";
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        } else{
            message="Error desconocido";
        }
        if (error.networkResponse!=null){
            try {
                message+="\n"+new String(error.networkResponse.data,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return message;
    }
    public SimpleMultiPartRequest uploadAudio(final String audioPath, final int userID, final audioUploadListener listener){
        Log.e("something", audioPath);
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL+"/uploadAudio.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String message = jObj.getString("message");
                            listener.onFileUploadSuccess(response);
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                listener.onFileUploadError(getVolleyError(error));
            }
        });

        smr.addMultipartParam("userID", "text/plain", ""+userID);
        smr.addMultipartParam("audio", "application/octet-stream", audioPath);
        //smr.addStringParam("userID", ""+userID);
        return smr;
    }




}