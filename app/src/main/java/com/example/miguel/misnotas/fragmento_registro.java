package com.example.miguel.misnotas;

/**
 * Created by Migue on 04/07/2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.example.miguel.misnotas.clases_alarma.Reactivar_Sync;
import com.example.miguel.misnotas.clases_alarma.Servicio_Sincronizar_Notas;

import java.util.Calendar;
import java.util.regex.Pattern;

public class fragmento_registro extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, Volley_Singleton.LoginListener, Volley_Singleton.NotesResponseListener{

    private TextInputLayout label_email, label_password, label_password_c, label_username;
    private EditText email, password, password_c, username;
    private Button submit;
    private Pattern regex_password;
    private AlertDialog mensaje;
    private AlertDialog.Builder aBuilder;
    private ProgressDialog progressDialog;
    private SharedPreferences ShPrSync;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private PackageManager packageManager;
    private ComponentName receiver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragmento_registro, container, false);
        label_username=(TextInputLayout)rootView.findViewById(R.id.label_username);
        label_email=(TextInputLayout)rootView.findViewById(R.id.label_email);
        label_password=(TextInputLayout) rootView.findViewById(R.id.label_password);
        label_password_c=(TextInputLayout)rootView.findViewById(R.id.label_password_c);
        username=(EditText)rootView.findViewById(R.id.username);
        email=(EditText) rootView.findViewById(R.id.email);
        password=(EditText) rootView.findViewById(R.id.password);
        password_c=(EditText)rootView.findViewById(R.id.password_c);
        submit=(Button) rootView.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        regex_password=regex_password.compile("^.{4,}$");
        username.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        password_c.setOnFocusChangeListener(this);
        password_c.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!ValidarPassword(password_c.getText())){
                        label_password_c.setError("La contraseña es incorrecta, debe tener al menos 4 caracteres");
                    }
                    else{
                        label_password_c.setError(null);
                        label_password_c.setErrorEnabled(false);
                    }

                }
                return false; // pass on to other listeners.
            }
        });
        mensaje = new AlertDialog.Builder(getActivity()).create();
        mensaje.setTitle("Notas de MigueLopez :D");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Notas de MigueLopez :D");
        aBuilder=new AlertDialog.Builder(getActivity()).setTitle("Notas de MigueLópez :D").setCancelable(true);
        ShPrSync= getActivity().getSharedPreferences("Sync", Context.MODE_PRIVATE);
        alarmManager=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        packageManager = getActivity().getPackageManager();
        receiver = new ComponentName(getActivity(), Reactivar_Sync.class);
        return rootView;
    }
    boolean ValidarUsername(){
        return (username.getText().length()==0)? false:true;
    }
    boolean ValidarEmail(){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches();
    }
    boolean ValidarPassword(CharSequence texto){
        return regex_password.matcher(texto).matches();
    }
    @Override
    public void onClick(View v) {
        if (ValidarEmail() && ValidarPassword(password.getText()) && ValidarUsername() && ValidarPassword(password_c.getText())){
            if (!(password_c.getText().toString().equals(password.getText().toString()))){
                mensaje.setMessage("Las contraseñas no coinciden");
                mensaje.show();
                return;
            }
            startSignUp();
            //Hacer registro
        }
        else{
            Toast.makeText(getActivity(), "Hay un error con los datos", Toast.LENGTH_SHORT).show();
        }

    }
    void startSignUp(){
        progressDialog.setMessage("Registrando....Por favor espere");
        progressDialog.show();
        Volley_Singleton.getInstance(getActivity()).Registrar(username.getText().toString(),email.getText().toString(),password.getText().toString(),this);
    }

    void StartDatabaseSync(){
        String NotasNoSync = Database.getInstance(getActivity()).crearJSON("SELECT * FROM notas WHERE subida='N'");
        String NotasSync = Database.getInstance(getActivity()).crearJSON("SELECT * FROM notas WHERE subida='S'");
        progressDialog.setMessage("Sincronizando...Por favor espere");
        progressDialog.show();
        Volley_Singleton.getInstance(getActivity()).syncDBLocal_Remota(NotasSync,NotasNoSync,ShPrSync.getInt("id_usuario", 1),ShPrSync.getInt("UltimoIDSync", 0), true, this);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus){
            switch (v.getId()){
                case R.id.username:
                    if (!ValidarUsername()){
                        label_username.setError("El nombre de usuario no puede estar vacio");
                    }
                    else{
                        label_username.setError(null);
                        label_username.setErrorEnabled(false);
                    }
                    break;
                case R.id.email:
                    if (!ValidarEmail()){
                        label_email.setError("El correo electrónico es incorrecto");
                    }
                    else{
                        label_email.setError(null);
                        label_email.setErrorEnabled(false);
                    }
                    break;
                case R.id.password:
                    if (!ValidarPassword(password.getText())){
                        label_password.setError("La contraseña es incorrecta, debe tener al menos 4 caracteres");
                    }
                    else{
                        label_password.setError(null);
                        label_password.setErrorEnabled(false);
                    }
                    break;
                case R.id.password_c:
                    if (!ValidarPassword(password_c.getText())){
                        label_password_c.setError("La contraseña es incorrecta, debe tener al menos 4 caracteres");
                    }
                    else{
                        label_password_c.setError(null);
                        label_password_c.setErrorEnabled(false);
                    }
                    break;
            }
        }
    }

    @Override
    public void onLoginSuccess(int id_usuario, String username, String email, int sync_time) {
        ShPrSync.edit().putInt("sync_time", sync_time).apply();
        progressDialog.dismiss();
        ShPrSync.edit().putInt("id_usuario",id_usuario).putString("username",username).putString("email",email).apply();
        StartDatabaseSync();
    }

    @Override
    public void onLoginError(String error) {
        progressDialog.dismiss();
        aBuilder.setMessage(error);
        aBuilder.show();
    }

    @Override
    public void activateAutoSync(int time) {
        //Se genera un intent para acceder a la clase del servicio
        Intent sync_service = new Intent(getActivity(), Servicio_Sincronizar_Notas.class);
        //Se crea el pendingintent que se necesita para el alarmmanager
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, sync_service, 0);
        //Se genera una instancia del calendario a una hora determinada
        Calendar calendar = Calendar.getInstance();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), time, pendingIntent);
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onSyncSuccess(int UltimoIDSync, int TotalNumberOfNotes) {
        progressDialog.dismiss();
        ShPrSync.edit().putInt("UltimoIDSync", UltimoIDSync).putInt("TotalNumberOfNotes", TotalNumberOfNotes).apply();
        Intent i=new Intent(getActivity(), Principal.class);
        getActivity().startActivity(i);
    }

    @Override
    public void onSyncError(String error) {
        progressDialog.dismiss();
        aBuilder.setMessage(error);
        aBuilder.show();
    }
}

