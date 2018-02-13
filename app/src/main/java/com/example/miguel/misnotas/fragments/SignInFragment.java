package com.example.miguel.misnotas.fragments;

/**
 * Created by Migue on 04/07/2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.VolleySingleton;
import com.example.miguel.misnotas.activities.MainActivity;
import com.example.miguel.misnotas.broadcasts.SyncNotesService;
import com.example.miguel.misnotas.broadcasts.bootservices.TurnOnDatabaseSync;
import com.example.miguel.misnotas.viewmodels.LoginActivityViewModel;

import java.util.Calendar;
import java.util.regex.Pattern;

public class SignInFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, VolleySingleton.LoginListener, VolleySingleton.NotesResponseListener {

    private TextInputLayout label_email, label_password;
    private EditText email, password;
    private Button submit;
    private Pattern regex_password;
    private AlertDialog.Builder aBuilder;
    private ProgressDialog progressDialog;
    private SharedPreferences ShPrSync;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private PackageManager packageManager;
    private ComponentName receiver;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        label_email = (TextInputLayout) rootView.findViewById(R.id.label_email);
        label_password = (TextInputLayout) rootView.findViewById(R.id.label_password);
        email = (EditText) rootView.findViewById(R.id.email);
        password = (EditText) rootView.findViewById(R.id.password);
        submit = (Button) rootView.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        regex_password = Pattern.compile("^.{4,}$");
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if (!ValidarPassword()) {
                        label_password.setError(getString(R.string.activity_login_incorrect_password));
                    } else {
                        label_password.setError(null);
                        label_password.setErrorEnabled(false);
                    }

                }
                return false; // pass on to other listeners.
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.dialog_default_title);
        aBuilder = new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_default_title).setCancelable(true);
        ShPrSync = getActivity().getSharedPreferences("Sync", Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        packageManager = getActivity().getPackageManager();
        receiver = new ComponentName(getActivity(), TurnOnDatabaseSync.class);
        loginActivityViewModel = ViewModelProviders.of(getActivity()).get(LoginActivityViewModel.class);
        password.setText(loginActivityViewModel.getSignInFragmentViewModel().getPassword());
        email.setText(loginActivityViewModel.getSignInFragmentViewModel().getEmail());
        return rootView;
    }

    boolean ValidarEmail() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches();
    }

    boolean ValidarPassword() {
        return regex_password.matcher(password.getText()).matches();
    }

    @Override
    public void onClick(View v) {

        if (ValidarEmail() && ValidarPassword()) {
            StartLogin();
        } else {
            Toast.makeText(getActivity(), R.string.activity_login_data_error, Toast.LENGTH_SHORT).show();
        }

    }

    /*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("email", email.getText().toString());
        savedInstanceState.putString("password", password.getText().toString());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null){
            email.setText(savedInstanceState.getString("email"));
            if (!ValidarEmail()){
                label_email.setError("El correo electrónico es incorrecto");
            }
            else{
                label_email.setError(null);
                label_email.setErrorEnabled(false);
            }
            //email.setText(savedInstanceState.getString("email"));
        }
    }
    */
    void StartLogin() {
        progressDialog.setMessage(getString(R.string.fragment_login_progress_dialog_label));
        progressDialog.show();
        VolleySingleton.getInstance(getActivity()).IniciarSesion(email.getText().toString(), password.getText().toString(), this);
        Log.d("test", "Starting session");
    }

    void StartDatabaseSync() {
        Log.d("test", "starting sync");
        String NotasNoSync = Database.getInstance(getActivity()).crearJSON("SELECT * FROM notas WHERE subida='N'");
        String NotasSync = Database.getInstance(getActivity()).crearJSON("SELECT * FROM notas WHERE subida='S'");
        progressDialog.setMessage(getString(R.string.syncing_label));
        progressDialog.show();
        VolleySingleton.getInstance(getActivity()).syncDBLocal_Remota(NotasSync, NotasNoSync, ShPrSync.getInt("userID", 1), ShPrSync.getInt("UltimoIDSync", 0), true, this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.email:
                    if (!ValidarEmail()) {
                        label_email.setError(getString(R.string.activity_login_incorrect_email));
                    } else {
                        label_email.setError(null);
                        label_email.setErrorEnabled(false);
                    }
                    break;
                case R.id.password:
                    if (!ValidarPassword()) {
                        label_password.setError(getString(R.string.activity_login_incorrect_password));
                    } else {
                        label_password.setError(null);
                        label_password.setErrorEnabled(false);
                    }
                    break;
            }
        }
    }

    @Override
    public void onLoginSuccess(int id_usuario, String username, String email, int sync_time) {
        Log.d("test", "session started");
        ShPrSync.edit().putInt("sync_time", sync_time).apply();
        //activateAutoSync(sync_time);
        progressDialog.dismiss();
        ShPrSync.edit().putInt("userID", id_usuario).putString("username", username).putString("email", email).apply();
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
        Intent sync_service = new Intent(getActivity(), SyncNotesService.class);
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
        Intent i = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(i);
    }

    @Override
    public void onSyncError(String error) {
        progressDialog.dismiss();
        aBuilder.setMessage(error);
        aBuilder.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        loginActivityViewModel.getSignInFragmentViewModel().setPassword(password.getText().toString());
        loginActivityViewModel.getSignInFragmentViewModel().setEmail(email.getText().toString());
    }
}

