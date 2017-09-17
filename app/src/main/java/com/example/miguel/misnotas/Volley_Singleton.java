package com.example.miguel.misnotas;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Volley_Singleton {

    private static Volley_Singleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context AppContext;
    private final String URL="http://miguelarcos.x10.mx/android/movil";

    public interface NotesResponseListener{
        void onSuccess(JSONArray response, int UltimoIDSync, int TotalNumberOfNotes);
        void onError(String error);
    }
    private Volley_Singleton(Context context) {
        this.AppContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Volley_Singleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Volley_Singleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(AppContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void syncDBLocal_Remota(final String NotasSync, final String NotasNoSync, final int id_usuario, final int UltimoIDSync, final NotesResponseListener listener){
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL+"/OperacionesBD.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        JSONArray array= null;
                        JSONObject SyncData= null;
                        try {
                            array=new JSONArray(response);
                            SyncData = array.getJSONObject(array.length()-1);
                            listener.onSuccess(array, SyncData.getInt("UltimoIDSync"), SyncData.getInt("TotalNumberOfNotes"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        String message = null;
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
                        listener.onError(message);
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
    /*
    public void Activar_Sincronizacion_Programada(){
        //Se genera un intent para acceder a la clase del servicio
        Intent sync_service = new Intent(AppContext, Servicio_Sincronizar_Notas.class);
        //Se crea el pendingintent que se necesita para el alarmmanager
        PendingIntent= PendingIntent.getBroadcast(AppContext, 0,sync_service,0);
        //Se genera una instancia del calendario a una hora determinada
        Calendar calendar = Calendar.getInstance();
        alarmas.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, PendingIntent);
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }*/

}