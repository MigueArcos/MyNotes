package com.example.miguel.misnotas;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.miguel.misnotas.DialogAudioRecord.AUDIO_RECORDER_FOLDER;

public class Volley_Singleton {

    private static Volley_Singleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context AppContext;
    //private final String URL="http://miguelarcos.x10.mx/android/movil";
    private final String HOST = "http://192.168.43.42";
    private final String ENDPOINT = "/android";
    private final String URL = HOST + ENDPOINT + "/movil";
    private final int SYNC_TIME = 3600000;

    public interface NotesResponseListener {
        void onSyncSuccess(int UltimoIDSync, int TotalNumberOfNotes);

        void onSyncError(String error);
    }

    public interface LoginListener {
        void onLoginSuccess(int id_usuario, String username, String email, int sync_time);

        void onLoginError(String error);

        void activateAutoSync(int time);
    }

    public interface audiosUploadListener {
        void onFilesUploadSuccess(String serverMessage);
        void noFilesToUpload();
        void onFilesUploadError(String serverMessage);
    }

    public interface audiosDownloadListener {
        void onFilesDownloadSuccess(File voiceNotesZip, File voiceNotesFolder);

        void onFilesDownloadError(String serverMessage);
    }

    public interface audioDeletionListener{
        void onNoteDeletionSuccess(String serverMessage);
        void onNoteDeletionError(String serverMessage);
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

    public void syncDBLocal_Remota(final String NotasSync, final String NotasNoSync, final int id_usuario, final int UltimoIDSync, final boolean isLogin, final NotesResponseListener listener) {
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL + "/OperacionesBD.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray array = null;
                        JSONObject SyncData = null;
                        try {
                            array = new JSONArray(response);
                            SyncData = array.getJSONObject(array.length() - 1);
                            Database.getInstance(AppContext).NotasServidorALocalDB(array, isLogin);
                            listener.onSyncSuccess(SyncData.getInt("UltimoIDSync"), SyncData.getInt("TotalNumberOfNotes"));
                            Log.d("JSON", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                if (!NotasNoSync.equals("")) {
                    params.put("NotasNoSyncJSON", NotasNoSync);
                }
                if (!NotasSync.equals("")) {
                    params.put("NotasSyncJSON", NotasSync);
                }
                params.put("id_usuario", String.valueOf(id_usuario));
                params.put("UltimoIDSync", String.valueOf(UltimoIDSync));
                return params;
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

    private String getVolleyError(VolleyError error) {
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
        } else {
            message = "Error desconocido";
        }
        if (error.networkResponse != null) {
            try {
                message += "\n" + new String(error.networkResponse.data, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    public void uploadAudios(int userID, List<File> voiceNotes, audiosUploadListener listener) {

        if (voiceNotes.size() == 0){
            listener.noFilesToUpload();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("userID", "" + userID);
//Add files to a request
        List<File> fileParams = new ArrayList<>();

        for (File voiceNote : voiceNotes){
            fileParams.add(voiceNote);
        }

// Add header to a request, if any
        Map<String, String> header = new HashMap<>();
        header.put("content-type", "multipart/form-data");
        VolleyMultipartRequest mMultipartRequest = new VolleyMultipartRequest(URL + "/uploadAudios.php",
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        listener.onFilesUploadError(getVolleyError(error));
                    }
                },
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onFilesUploadSuccess(response);

                    }
                }, fileParams, params, header, "audios[]"
        );
        mMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
/**
 * adding request to queue
 */
        try {
            try {
                Log.e("POPO", mMultipartRequest.getHeaders().toString()+"\n"+mMultipartRequest.getUrl()+"\n"+mMultipartRequest.getBodyContentType()+"\n"+new String(mMultipartRequest.getBody(),"UTF-8")+ "\n" +mMultipartRequest.toString() + "\n"  + params.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        addToRequestQueue(mMultipartRequest);
    }

    public void downloadAudio(final int userID, final audiosDownloadListener listener) {

        VolleyDownloader request = new VolleyDownloader(Request.Method.GET, HOST + ENDPOINT + "/" + AUDIO_RECORDER_FOLDER + "/" +userID+"/voiceNotes.zip",
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // TODO handle the response
                        try {
                            if (response != null) {
                                FileOutputStream outputStream;
                                String voiceNotesPath = Environment.getExternalStorageDirectory().getPath() + "/"+AUDIO_RECORDER_FOLDER;
                                String zipFilePath = voiceNotesPath  + "/voiceNotes.zip";
                                File voiceNotesFolder = new File(voiceNotesPath);
                                MyUtils.deleteFilesInFolder(voiceNotesFolder);
                                File voiceNotesZip = new File(zipFilePath);
                                outputStream = new FileOutputStream(voiceNotesZip, true);
                                outputStream.write(response);
                                outputStream.close();
                                listener.onFilesDownloadSuccess(voiceNotesZip, voiceNotesFolder);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                if (error instanceof ServerError) {
                    listener.onFilesDownloadError("Empty");
                }else{
                    listener.onFilesDownloadError(getVolleyError(error));
                }

            }
        }, null);
        addToRequestQueue(request);
    }

    public void deleteVoiceNote(final int userID, final String audioName, final audioDeletionListener listener) {
        StringRequest MyRequest = new StringRequest(Request.Method.POST, URL + "/deleteAudio.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Archivo eliminado")){
                            listener.onNoteDeletionSuccess(response);
                        }
                        else {
                            listener.onNoteDeletionError(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        listener.onNoteDeletionError(getVolleyError(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", String.valueOf(userID));
                params.put("audioName", audioName);
                return params;
            }
        };
        addToRequestQueue(MyRequest);
    }
}