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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.InputField;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.VolleySingleton;
import com.example.miguel.misnotas.activities.MainActivity;
import com.example.miguel.misnotas.broadcasts.SyncNotesService;
import com.example.miguel.misnotas.broadcasts.bootservices.TurnOnDatabaseSync;
import com.example.miguel.misnotas.models.SyncData;
import com.example.miguel.misnotas.viewmodels.LoginActivityViewModel;

import java.util.Calendar;
import java.util.regex.Pattern;

public class SignInFragment extends Fragment implements View.OnClickListener, VolleySingleton.LoginListener, VolleySingleton.NotesResponseListener {

    private InputField email, password;
    private AlertDialog.Builder message;
    private ProgressDialog progressDialog;
    private SharedPreferences ShPrSync;
    private AlarmManager alarmManager;
    private PackageManager packageManager;
    private ComponentName receiver;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        email = rootView.findViewById(R.id.email);
        email.setRegex(Patterns.EMAIL_ADDRESS);
        email.setErrorLabel(getString(R.string.activity_login_incorrect_email));

        password = rootView.findViewById(R.id.password);
        password.setRegex(Pattern.compile("^.{4,}$"));
        password.setErrorLabel(getString(R.string.activity_login_incorrect_password));
        password.setAsLastField();

        Button submit = rootView.findViewById(R.id.submit_button);
        submit.setOnClickListener(this);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.dialog_default_title);
        message = new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_default_title).setCancelable(true);
        ShPrSync = getActivity().getSharedPreferences("sync", Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        packageManager = getActivity().getPackageManager();
        receiver = new ComponentName(getActivity(), TurnOnDatabaseSync.class);

        loginActivityViewModel = ViewModelProviders.of(getActivity()).get(LoginActivityViewModel.class);
        password.setText(loginActivityViewModel.getSignInFragmentViewModel().getPassword());
        email.setText(loginActivityViewModel.getSignInFragmentViewModel().getEmail());
        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (email.validateField() && password.validateField()) {
            StartLogin();
        } else {
            Toast.makeText(getActivity(), R.string.activity_login_data_error, Toast.LENGTH_SHORT).show();
        }

    }


    void StartLogin() {
        progressDialog.setMessage(getString(R.string.fragment_sign_in_progress_dialog_label));
        progressDialog.show();
        VolleySingleton.getInstance(getActivity()).IniciarSesion(email.getText(), password.getText(), this);
    }

    void StartDatabaseSync() {
        progressDialog.setMessage(getString(R.string.syncing_label));
        progressDialog.show();
        SyncData localSyncData = Database.getInstance(getActivity()).createLocalSyncData(new SyncData.SyncInfo(ShPrSync.getInt("userId", 1), ShPrSync.getInt("lastSyncedId", 0)));
        VolleySingleton.getInstance(getActivity()).syncDatabases(localSyncData, false, this);
    }

    @Override
    public void onLoginSuccess(int userId, String username, String email, int syncTime) {
        ShPrSync.edit().putInt("syncTime", syncTime).apply();
        activateAutoSync(syncTime);
        progressDialog.dismiss();
        ShPrSync.edit().putInt("userId", userId).putString("username", username).putString("email", email).apply();


        StartDatabaseSync();
    }

    @Override
    public void onLoginError(String error) {
        progressDialog.dismiss();
        message.setMessage(error);
        message.show();
    }

    @Override
    public void activateAutoSync(int time) {
        //Se genera un intent para acceder a la clase del servicio
        Intent syncService = new Intent(getActivity(), SyncNotesService.class);
        //Se crea el pendingintent que se necesita para el alarmmanager
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, syncService, 0);
        //Se genera una instancia del calendario a una hora determinada
        Calendar calendar = Calendar.getInstance();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), time, pendingIntent);
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onSyncSuccess(SyncData.SyncInfo syncInfo) {
        progressDialog.dismiss();
        ShPrSync.edit().putInt("lastSyncedId", syncInfo.getLastSyncedId()).apply();
        Intent i = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(i);
    }

    @Override
    public void onSyncError(String error) {
        progressDialog.dismiss();
        message.setMessage(error);
        message.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        loginActivityViewModel.getSignInFragmentViewModel().setPassword(password.getText());
        loginActivityViewModel.getSignInFragmentViewModel().setEmail(email.getText());
    }
}

