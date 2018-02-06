/*package com.example.miguel.misnotas;

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|                       This class has beeen deprecated on 21/09/2017                              |
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 We will let it because we could need it later

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

import com.example.miguel.misnotas.clases_alarma.Reactivar_Sync;
import com.example.miguel.misnotas.clases_alarma.Servicio_Sincronizar_Notas;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

/*
    * Created by Migue on 01/07/2017.
    *
*/
/*
public class Sincronizacion {
    private Context ActivityContext;
    private SharedPreferences ShPrSync;
    private SharedPreferences.Editor Editor;
    private ProgressDialog progreso;
    private AlertDialog mensaje;
    private AlarmManager alarmas;
    private PendingIntent PendingIntent;
    private PackageManager packageManager;
    private ComponentName receiver;
    private final String URL="http://miguelarcos.x10.mx/android/movil";
    public Sincronizacion(Context ActivityContext){
        this.ActivityContext=ActivityContext;
        ShPrSync= ActivityContext.getSharedPreferences("Sync", Context.MODE_PRIVATE);
        Editor=ShPrSync.edit();
        //Initialize Progress Dialog properties
        progreso = new ProgressDialog(ActivityContext);
        progreso.setCancelable(false);
        progreso.setTitle("Notas de MigueLopez :D");
        mensaje = new AlertDialog.Builder(ActivityContext).create();
        mensaje.setTitle("Notas de MigueLopez :D");
        alarmas=(AlarmManager)ActivityContext.getSystemService(Context.ALARM_SERVICE);
        packageManager = ActivityContext.getPackageManager();
        receiver = new ComponentName(ActivityContext, ReactivateDatabaseSync.class);
        //mensaje.setMessage("Esta App fue programada por Miguel Ángel López Arcos x'D");
        //Se retorna la vista del fragmento que se creo
    }
    public void CrearJSON_SincronizarBDs(final Fragment fragment){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String NotasNoSync=ob.crearJSON("SELECT * FROM notas WHERE subida='N'");
        String NotasSync=ob.crearJSON("SELECT * FROM notas WHERE subida='S'");
        //Toast.makeText(getActivity(), NotasSync, Toast.LENGTH_LONG).show();
        progreso.setMessage("Sincronizando...Por favor espere");
        progreso.show();
        if (!NotasNoSync.equals("")){
            params.put("NotasNoSyncJSON", NotasNoSync);
        }
        if (!NotasSync.equals("")){
            params.put("NotasSyncJSON", NotasSync);
            Log.d("json",NotasSync);
        }
        params.put("userID",ShPrSync.getInt("userID", 1));
        params.put("UltimoIDSync", ShPrSync.getInt("UltimoIDSync", 0));
        client.setConnectTimeout(10000);
        client.post(URL+"/CrearJSON.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                progreso.hide();
                try {
                    response = (responseBody == null) ? null : new String(responseBody, getCharset());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONArray array=null;
                try {
                    array = new JSONArray(response);
                    JSONObject SyncData=array.getJSONObject(array.length()-1);
                    Editor.putInt("UltimoIDSync", SyncData.getInt("UltimoIDSync"));
                    Editor.putInt("TotalNumberOfNotes", SyncData.getInt("TotalNumberOfNotes"));
                    Editor.commit();
                    if ((array.length()-1)==array.getJSONObject(array.length()-1).getInt("TotalNumberOfNotes")){
                        syncDBLocal_Remota(fragment,response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                //mensaje.setMessage(response);
                //mensaje.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                progreso.hide();
                if(statusCode == 404){
                    Toast.makeText(ActivityContext, "No se encontro el recurso buscado :(", Toast.LENGTH_LONG).show();
                }else if(statusCode == 500){
                    Toast.makeText(ActivityContext, "Ooops, algo salió mal :( popo", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ActivityContext, "Ocurrió algun error (Puede ser que no este conectado a Internet)", Toast.LENGTH_LONG).show();
                }
            }

        });

    }
    public void CrearJSON_SincronizarBDs(){
        //Create AsycHttpClient object
        Toast.makeText(ActivityContext,"Hijo de la verga", Toast.LENGTH_SHORT).show();
        mensaje.setMessage("Hola hijo de la verga");
        mensaje.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String NotasNoSync=Database.getInstance(ActivityContext).crearJSON("SELECT * FROM notas WHERE subida='N'");
        String NotasSync=Database.getInstance(ActivityContext).crearJSON("SELECT * FROM notas WHERE subida='S'");
        if (!NotasNoSync.equals("")){
            params.put("NotasNoSyncJSON", NotasNoSync);
        }
        if (!NotasSync.equals("")){
            params.put("NotasSyncJSON", NotasSync);
            Log.d("json",NotasSync);
        }
        params.put("userID",ShPrSync.getInt("userID", 1));
        params.put("UltimoIDSync", ShPrSync.getInt("UltimoIDSync", 0));
        client.setConnectTimeout(10000);
        client.post(URL+"/CrearJSON.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = (responseBody == null) ? null : new String(responseBody, getCharset());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONArray array=null;
                try {
                    array = new JSONArray(response);
                    JSONObject SyncData=array.getJSONObject(array.length()-1);
                    Editor.putInt("UltimoIDSync", SyncData.getInt("UltimoIDSync"));
                    Editor.putInt("TotalNumberOfNotes", SyncData.getInt("TotalNumberOfNotes"));
                    Editor.commit();
                    if ((array.length()-1)==array.getJSONObject(array.length()-1).getInt("TotalNumberOfNotes")){
                        //syncDBLocal_Remota(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                //mensaje.setMessage(response);
                //mensaje.show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });

    }
    public void syncDBLocal_Remota(final Fragment fragment, final String JSONCompleto){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        progreso.setMessage("Sincronizando...Por favor espere");
        progreso.show();
        client.setConnectTimeout(10000);
        params.put("userID",ShPrSync.getInt("userID", 0));
        params.put("JSONCompleto",JSONCompleto);
        client.post(URL+"/SincronizarBDs.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progreso.hide();
                JSONArray array=null;
                try {
                    array = new JSONArray(JSONCompleto);
                    JSONObject SyncData=array.getJSONObject(array.length()-1);
                    Editor.putInt("UltimoIDSync", SyncData.getInt("UltimoIDSync"));
                    Editor.putInt("TotalNumberOfNotes", SyncData.getInt("TotalNumberOfNotes"));
                    Editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ob.NotasServidorALocalDB(array);
                fragment.onResume();

                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                //mensaje.setMessage(response);
                //mensaje.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                progreso.hide();
                if(statusCode == 404){
                    Toast.makeText(ActivityContext, "No se encontro el recurso buscado :(", Toast.LENGTH_LONG).show();
                }else if(statusCode == 500){
                    Toast.makeText(ActivityContext, "Ooops, algo salió mal :(", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ActivityContext, "Ocurrió algun error (Puede ser que no este conectado a Internet)", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    public void syncDBLocal_Remota(final String JSONCompleto){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.setConnectTimeout(10000);
        params.put("userID",ShPrSync.getInt("userID", 0));
        params.put("JSONCompleto",JSONCompleto);
        client.post(URL+"/SincronizarBDs.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONArray array=null;
                try {
                    array = new JSONArray(JSONCompleto);
                    JSONObject SyncData=array.getJSONObject(array.length()-1);
                    Editor.putInt("UltimoIDSync", SyncData.getInt("UltimoIDSync"));
                    Editor.putInt("TotalNumberOfNotes", SyncData.getInt("TotalNumberOfNotes"));
                    Editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Database.getInstance(ActivityContext).NotasServidorALocalDB(array);
                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                //mensaje.setMessage(response);
                //mensaje.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
            }

        });
    }
    public void syncDBLocal_Remota(final Intent intent){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String NotasNoSync=Database.getInstance(ActivityContext).crearJSON("SELECT * FROM notas WHERE subida='N'");
        String NotasSync=Database.getInstance(ActivityContext).crearJSON("SELECT * FROM notas WHERE subida='S'");
        //Toast.makeText(getActivity(), NotasSync, Toast.LENGTH_LONG).show();
        progreso.setMessage("Sincronizando...Por favor espere");
        progreso.show();
        if (!NotasNoSync.equals("")){
            params.put("NotasNoSyncJSON", NotasNoSync);
        }
        if (!NotasSync.equals("")){
            params.put("NotasSyncJSON", NotasSync);
        }
        params.put("userID",ShPrSync.getInt("userID", 1));
        params.put("UltimoIDSync", ShPrSync.getInt("UltimoIDSync", 0));
        client.post(URL+"/OperacionesBD.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progreso.hide();
                String response = null;
                try {
                    response = (responseBody == null) ? null : new String(responseBody, getCharset());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //int ResponseSize=response.length();
                //int NewLastSyncID=0;
                for (int i=ResponseSize-1; i>=0; i--){
                    if (response.charAt(i)==','){
                        NewLastSyncID=Integer.parseInt(response.substring(i+1,ResponseSize));
                        response=response.substring(0,i);
                        break;
                    }
                }
                JSONArray array=null;
                try {
                    array = new JSONArray(response);
                    JSONObject SyncData=array.getJSONObject(array.length()-1);
                    Editor.putInt("UltimoIDSync", SyncData.getInt("UltimoIDSync"));
                    Editor.putInt("TotalNumberOfNotes", SyncData.getInt("TotalNumberOfNotes"));
                    Editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Activar_Sincronizacion_Programada();
                Database.getInstance(ActivityContext).NotasServidorALocalDB(array);
                ActivityContext.startActivity(intent);
                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                //mensaje.setMessage(response);
                // mensaje.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                progreso.hide();
                if(statusCode == 404){
                    Toast.makeText(ActivityContext, "No se encontro el recurso buscado :(", Toast.LENGTH_LONG).show();
                }else if(statusCode == 500){
                    Toast.makeText(ActivityContext, "Ooops, algo salió mal :(", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ActivityContext, "Ocurrió algun error (Puede ser que no este conectado a Internet)", Toast.LENGTH_LONG).show();
                }
            }

        });

    }
    public void syncDBLocal_Remota(final Fragment fragment){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String NotasNoSync=Database.getInstance(ActivityContext).crearJSON("SELECT * FROM notas WHERE subida='N'");
        String NotasSync=Database.getInstance(ActivityContext).crearJSON("SELECT * FROM notas WHERE subida='S'");
        //Toast.makeText(getActivity(), NotasSync, Toast.LENGTH_LONG).show();
        progreso.setMessage("Sincronizando...Por favor espere");
        progreso.show();
        if (!NotasNoSync.equals("")){
            params.put("NotasNoSyncJSON", NotasNoSync);
        }
        if (!NotasSync.equals("")){
            params.put("NotasSyncJSON", NotasSync);
        }
        params.put("userID",ShPrSync.getInt("userID", 1));
        params.put("UltimoIDSync", ShPrSync.getInt("UltimoIDSync", 0));
        client.post(URL+"/OperacionesBD.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progreso.hide();
                String response = null;
                try {
                    response = (responseBody == null) ? null : new String(responseBody, getCharset());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //int ResponseSize=response.length();
                //int NewLastSyncID=0;
                for (int i=ResponseSize-1; i>=0; i--){
                    if (response.charAt(i)==','){
                        NewLastSyncID=Integer.parseInt(response.substring(i+1,ResponseSize));
                        response=response.substring(0,i);
                        break;
                    }
                }
                JSONArray array=null;
                try {
                    array = new JSONArray(response);
                    JSONObject SyncData=array.getJSONObject(array.length()-1);
                    Editor.putInt("UltimoIDSync", SyncData.getInt("UltimoIDSync"));
                    Editor.putInt("TotalNumberOfNotes", SyncData.getInt("TotalNumberOfNotes"));
                    Editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Activar_Sincronizacion_Programada();
                Database.getInstance(ActivityContext).NotasServidorALocalDB(array);
                fragment.onResume();
                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                //mensaje.setMessage(response);
                // mensaje.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                progreso.hide();
                if(statusCode == 404){
                    Toast.makeText(ActivityContext, "No se encontro el recurso buscado :(", Toast.LENGTH_LONG).show();
                }else if(statusCode == 500){
                    Toast.makeText(ActivityContext, "Ooops, algo salió mal :(", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ActivityContext, "Ocurrió algun error (Puede ser que no este conectado a Internet)", Toast.LENGTH_LONG).show();
                }
            }

        });

    }
    public void IniciarSesion(String email, String password){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        progreso.setMessage("Iniciando sesión...Por favor espere");
        progreso.show();
        params.put("email",email);
        params.put("password",password);
        client.post(URL+"/IniciarSesion.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progreso.hide();
                String response = null;
                try {
                    response = (responseBody == null) ? null : new String(responseBody, getCharset());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(ActivityContext, response, Toast.LENGTH_SHORT).show();
                if (!response.equals("No encontrado")){
                    try {
                        JSONObject respuesta=new JSONObject(response);
                        //Toast.makeText(ActivityContext,, Toast.LENGTH_SHORT).show();
                        //mensaje.setMessage(""+respuesta.getInt("userID"));
                        //mensaje.show();
                        Editor.putInt("userID",respuesta.getInt("userID"));
                        Editor.putString("username", respuesta.getString("username"));
                        Editor.putString("email", respuesta.getString("email"));
                        Editor.commit();
                        Intent i =new Intent(ActivityContext,Principal.class);
                        syncDBLocal_Remota(i);
                        //return ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    mensaje.setMessage("Datos de usuario no encontrados");
                    mensaje.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                if(statusCode == 404){
                    Toast.makeText(ActivityContext, "No se encontro el recurso buscado :(", Toast.LENGTH_LONG).show();
                }else if(statusCode == 500){
                    Toast.makeText(ActivityContext, "Ooops, algo salió mal :(", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ActivityContext, "Ocurrió algun error (Puede ser que no este conectado a Internet)", Toast.LENGTH_LONG).show();
                }
            }

        });

    }
    public void Registrar(String username, String email, String password){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        progreso.setMessage("Registrando...Por favor espere");
        progreso.show();
        params.put("email",email);
        params.put("password",password);
        params.put("username",username);
        client.post(URL+"/Registrar.php",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progreso.hide();
                String response = null;
                try {
                    response = (responseBody == null) ? null : new String(responseBody, getCharset());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (!response.equals("Email repetido")){
                    try {
                        JSONObject respuesta=new JSONObject(response);
                        //Toast.makeText(ActivityContext,, Toast.LENGTH_SHORT).show();
                        //mensaje.setMessage(""+respuesta.getInt("userID"));
                        //mensaje.show();
                        Editor.putInt("userID",respuesta.getInt("userID"));
                        Editor.putString("username", respuesta.getString("username"));
                        Editor.putString("email", respuesta.getString("email"));
                        Editor.commit();
                        Intent i =new Intent(ActivityContext,Principal.class);
                        syncDBLocal_Remota(i);
                        //return ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    mensaje.setMessage("Este correo electrónico ya ha sido registrado");
                    mensaje.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                progreso.hide();
                if(statusCode == 404){
                    Toast.makeText(ActivityContext, "No se encontro el recurso buscado :(", Toast.LENGTH_LONG).show();
                }else if(statusCode == 500){
                    Toast.makeText(ActivityContext, "Ooops, algo salió mal :(", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ActivityContext, "Ocurrió algun error (Puede ser que no este conectado a Internet)", Toast.LENGTH_LONG).show();
                }
            }

        });

    }
    public void CerrarSesion(){
        Editor.clear();
        Editor.commit();
        Database.getInstance(ActivityContext).VaciarNotas();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
    public void Activar_Sincronizacion_Programada(){
        //Se genera un intent para acceder a la clase del servicio
        Intent sync_service = new Intent(ActivityContext, SyncNotesService.class);
        //Se crea el pendingintent que se necesita para el alarmmanager
        PendingIntent= PendingIntent.getBroadcast(ActivityContext, 0,sync_service,0);
        //Se genera una instancia del calendario a una hora determinada
        Calendar calendar = Calendar.getInstance();
        alarmas.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, PendingIntent);
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }
}
*/