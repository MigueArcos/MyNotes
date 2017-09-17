package com.example.miguel.misnotas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miguel.misnotas.clases_alarma.Reactivar_Sync;
import com.example.miguel.misnotas.clases_alarma.Servicio_Sincronizar_Notas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Volley_Singleton {
    private static Volley_Singleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context AppContext;
    private SharedPreferences ShPrSync;
    private SharedPreferences.Editor Editor;
    private ProgressDialog progreso;
    private AlertDialog mensaje;
    private Base_Datos ob;
    private AlarmManager alarmas;
    private android.app.PendingIntent PendingIntent;
    private PackageManager packageManager;
    private ComponentName receiver;
    private final String URL="http://miguelarcos.x10.mx/android/movil";
    private Volley_Singleton(Context context) {
        this.AppContext = context;
        mRequestQueue = getRequestQueue();
        ob = new Base_Datos(AppContext);
        ShPrSync= AppContext.getSharedPreferences("Sync", Context.MODE_PRIVATE);
        Editor=ShPrSync.edit();
        //Initialize Progress Dialog properties
        progreso = new ProgressDialog(AppContext);
        progreso.setCancelable(false);
        progreso.setTitle("Notas de MigueLopez :D");
        mensaje = new AlertDialog.Builder(AppContext).create();
        mensaje.setTitle("Notas de MigueLopez :D");
        alarmas=(AlarmManager)AppContext.getSystemService(Context.ALARM_SERVICE);
        packageManager = AppContext.getPackageManager();
        receiver = new ComponentName(AppContext, Reactivar_Sync.class);
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
    public void syncDBLocal_Remota(final Fragment fragment){
        progreso.setMessage("Sincronizando...Por favor espere");
        progreso.show();
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL+"/OperacionesBD.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        progreso.dismiss();
                        JSONArray array= null;
                        JSONObject SyncData= null;
                        try {
                            array=new JSONArray(response);
                            SyncData = array.getJSONObject(array.length()-1);
                            Editor.putInt("UltimoIDSync", SyncData.getInt("UltimoIDSync"));
                            Editor.putInt("TotalNumberOfNotes", SyncData.getInt("TotalNumberOfNotes"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ob.NotasServidorALocalDB(array);
                        fragment.onResume();
                        Editor.commit();
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
                        }
                        progreso.dismiss();
                        mensaje.setMessage(message);
                        mensaje.show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                String NotasNoSync=ob.crearJSON("SELECT * FROM notas WHERE subida='N'");
                String NotasSync=ob.crearJSON("SELECT * FROM notas WHERE subida='S'");
                //Toast.makeText(getActivity(), NotasSync, Toast.LENGTH_LONG).show();
                if (!NotasNoSync.equals("")){
                    params.put("NotasNoSyncJSON", NotasNoSync);
                }
                if (!NotasSync.equals("")){
                    params.put("NotasSyncJSON", NotasSync);
                }
                params.put("id_usuario",""+ShPrSync.getInt("id_usuario", 1));
                params.put("UltimoIDSync", ""+ShPrSync.getInt("UltimoIDSync", 0));
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }
    public void syncDBLocal_Remota(final Intent intent){
        progreso.setMessage("Sincronizando...Por favor espere");
        progreso.show();
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL+"/OperacionesBD.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        progreso.dismiss();
                        JSONArray array= null;
                        JSONObject SyncData= null;
                        try {
                            array=new JSONArray(response);
                            SyncData = array.getJSONObject(array.length()-1);
                            Editor.putInt("UltimoIDSync", SyncData.getInt("UltimoIDSync"));
                            Editor.putInt("TotalNumberOfNotes", SyncData.getInt("TotalNumberOfNotes"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ob.NotasServidorALocalDB(array);
                        Editor.commit();
                        AppContext.startActivity(intent);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        progreso.dismiss();
                        mensaje.setMessage(error.toString());
                        mensaje.show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                String NotasNoSync=ob.crearJSON("SELECT * FROM notas WHERE subida='N'");
                String NotasSync=ob.crearJSON("SELECT * FROM notas WHERE subida='S'");
                //Toast.makeText(getActivity(), NotasSync, Toast.LENGTH_LONG).show();
                if (!NotasNoSync.equals("")){
                    params.put("NotasNoSyncJSON", NotasNoSync);
                }
                if (!NotasSync.equals("")){
                    params.put("NotasSyncJSON", NotasSync);
                }
                params.put("id_usuario",""+ShPrSync.getInt("id_usuario", 1));
                params.put("UltimoIDSync", ""+ShPrSync.getInt("UltimoIDSync", 0));
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }
    public void syncDBLocal_Remota(){
               StringRequest MyRequest = new StringRequest(Request.Method.POST, URL+"/OperacionesBD.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        JSONArray array= null;
                        JSONObject SyncData= null;
                        try {
                            array=new JSONArray(response);
                            SyncData = array.getJSONObject(array.length()-1);
                            Editor.putInt("UltimoIDSync", SyncData.getInt("UltimoIDSync"));
                            Editor.putInt("TotalNumberOfNotes", SyncData.getInt("TotalNumberOfNotes"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ob.NotasServidorALocalDB(array);
                        Editor.commit();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                String NotasNoSync=ob.crearJSON("SELECT * FROM notas WHERE subida='N'");
                String NotasSync=ob.crearJSON("SELECT * FROM notas WHERE subida='S'");
                //Toast.makeText(getActivity(), NotasSync, Toast.LENGTH_LONG).show();
                if (!NotasNoSync.equals("")){
                    params.put("NotasNoSyncJSON", NotasNoSync);
                }
                if (!NotasSync.equals("")){
                    params.put("NotasSyncJSON", NotasSync);
                }
                params.put("id_usuario",""+ShPrSync.getInt("id_usuario", 1));
                params.put("UltimoIDSync", ""+ShPrSync.getInt("UltimoIDSync", 0));
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }
    public void CerrarSesion(){
        Editor.clear();
        Editor.commit();
        ob.VaciarNotas();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
    public void Activar_Sincronizacion_Programada(){
        //Se genera un intent para acceder a la clase del servicio
        Intent sync_service = new Intent(AppContext, Servicio_Sincronizar_Notas.class);
        //Se crea el pendingintent que se necesita para el alarmmanager
        PendingIntent= PendingIntent.getBroadcast(AppContext, 0,sync_service,0);
        //Se genera una instancia del calendario a una hora determinada
        Calendar calendar = Calendar.getInstance();
        alarmas.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, PendingIntent);
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

}