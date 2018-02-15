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
import com.example.miguel.misnotas.viewmodels.LoginActivityViewModel;

import java.util.Calendar;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment implements View.OnClickListener, VolleySingleton.LoginListener, VolleySingleton.NotesResponseListener {

    private InputField username, email, password, confirmPassword;

    private AlertDialog message;
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
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Pattern regexPassword = Pattern.compile("^.{4,}$");

        username = rootView.findViewById(R.id.username);
        username.setRegex(regexPassword);
        username.setErrorLabel(getString(R.string.fragment_sign_up_incorrect_username));

        email = rootView.findViewById(R.id.email);
        email.setRegex(Patterns.EMAIL_ADDRESS);
        email.setErrorLabel(getString(R.string.activity_login_incorrect_email));

        password = rootView.findViewById(R.id.password);
        password.setRegex(regexPassword);
        password.setErrorLabel(getString(R.string.activity_login_incorrect_password));

        confirmPassword = rootView.findViewById(R.id.confirm_password);
        confirmPassword.setRegex(regexPassword);
        confirmPassword.setErrorLabel(getString(R.string.activity_login_incorrect_password));
        confirmPassword.setAsLastField();


        Button submit = rootView.findViewById(R.id.submit_button);
        submit.setOnClickListener(this);

        message = new AlertDialog.Builder(getActivity()).create();
        message.setTitle(R.string.dialog_default_title);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.dialog_default_title);

        ShPrSync = getActivity().getSharedPreferences("Sync", Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        packageManager = getActivity().getPackageManager();
        receiver = new ComponentName(getActivity(), TurnOnDatabaseSync.class);

        loginActivityViewModel = ViewModelProviders.of(getActivity()).get(LoginActivityViewModel.class);

        password.getEditText().setText(loginActivityViewModel.getSignUpFragmentViewModel().getPassword());
        email.getEditText().setText(loginActivityViewModel.getSignUpFragmentViewModel().getEmail());
        confirmPassword.getEditText().setText(loginActivityViewModel.getSignUpFragmentViewModel().getConfirmedPassword());
        username.getEditText().setText(loginActivityViewModel.getSignUpFragmentViewModel().getUserName());
        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (username.validateField() && email.validateField() && password.validateField() && confirmPassword.validateField()) {
            if (!(password.getText().equals(confirmPassword.getText()))) {
                message.setMessage(getString(R.string.fragment_sign_up_mismatch_passwords));
                message.show();
                return;
            }
            startSignUp();
            //Hacer registro
        } else {
            Toast.makeText(getActivity(), R.string.activity_login_data_error, Toast.LENGTH_SHORT).show();
        }

    }

    void startSignUp() {
        progressDialog.setMessage(getString(R.string.fragment_sign_up_progress_dialog_label));
        progressDialog.show();
        VolleySingleton.getInstance(getActivity()).Registrar(username.getText(), email.getText(), password.getText(), this);
    }

    void StartDatabaseSync() {
        String syncedNotes = Database.getInstance(getActivity()).crearJSON("SELECT * FROM notas WHERE subida='N'");
        String noSyncedNotes = Database.getInstance(getActivity()).crearJSON("SELECT * FROM notas WHERE subida='S'");
        progressDialog.setMessage(getString(R.string.syncing_label));
        progressDialog.show();
        VolleySingleton.getInstance(getActivity()).syncDBLocal_Remota(noSyncedNotes, syncedNotes, ShPrSync.getInt("userID", 1), ShPrSync.getInt("UltimoIDSync", 0), true, this);
    }


    @Override
    public void onLoginSuccess(int userId, String username, String email, int syncTime) {
        ShPrSync.edit().putInt("sync_time", syncTime).apply();
        progressDialog.dismiss();
        ShPrSync.edit().putInt("userID", userId).putString("username", username).putString("email", email).apply();
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
    public void onSyncSuccess(int UltimoIDSync, int TotalNumberOfNotes) {
        progressDialog.dismiss();
        ShPrSync.edit().putInt("UltimoIDSync", UltimoIDSync).putInt("TotalNumberOfNotes", TotalNumberOfNotes).apply();
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
        loginActivityViewModel.getSignUpFragmentViewModel().setPassword(password.getText());
        loginActivityViewModel.getSignUpFragmentViewModel().setEmail(email.getText());
        loginActivityViewModel.getSignUpFragmentViewModel().setConfirmedPassword(confirmPassword.getText());
        loginActivityViewModel.getSignUpFragmentViewModel().setUserName(username.getText());
    }
}