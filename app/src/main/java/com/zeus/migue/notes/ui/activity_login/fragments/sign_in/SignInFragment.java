package com.zeus.migue.notes.ui.activity_login.fragments.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.ui.activity_login.LoginActivityViewModel;
import com.zeus.migue.notes.ui.activity_main.MainActivity;
import com.zeus.migue.notes.ui.shared.BaseFragment;
import com.zeus.migue.notes.ui.shared.components.InputField;
import com.zeus.migue.notes.ui.shared.components.LoaderDialog;

import java.util.regex.Pattern;

public class SignInFragment extends BaseFragment implements View.OnClickListener {
    private InputField email, password;
    private LoaderDialog loader;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_login_fragment_sign_in, container, false);
        initializeViews(rootView);

        if (getActivity() != null) {
            loginActivityViewModel = new ViewModelProvider(getActivity()).get(LoginActivityViewModel.class);
            loginActivityViewModel.getEvent().observe(getViewLifecycleOwner(), liveDataEvent -> {
                if (loader != null) loader.dismiss();
                Event event = liveDataEvent.getContentIfNotHandled();
                if (event != null) {
                    String message = event.getLocalResId() == 0 ? event.getMessage() : getString(event.getLocalResId());
                    if (event.getMessageType() == Event.MessageType.SHOW_IN_DIALOG) {
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.generic_error_title).setMessage(message).show();
                    } else if (event.getMessageType() == Event.MessageType.SHOW_IN_TOAST) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                }
            });
            loginActivityViewModel.getNetworkResponse().observe(getViewLifecycleOwner(), networkResponse -> {
                switch (networkResponse){
                    case LoginActivityViewModel.LOGIN_SUCCESS:
                        loader.setMessage(getString(R.string.sync_loader_message));
                        break;
                    case LoginActivityViewModel.SYNC_SUCCESS:
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        loader.dismiss();
                        break;
                    default:
                        loader.dismiss();
                        break;
                }
            });
            password.setText(loginActivityViewModel.getSignInFragmentState().getPassword());
            email.setText(loginActivityViewModel.getSignInFragmentState().getEmail());
        }
        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (email.textIsValid() && password.textIsValid()) {
            loader = LoaderDialog.newInstance(getString(R.string.activity_login_fragment_sign_in_loader_label));
            loader.setCancelable(false);
            loader.show(getActivity().getSupportFragmentManager(), "dialog");
            loginActivityViewModel.signIn(email.getText(), password.getText());
        } else {
            Toast.makeText(getActivity(), R.string.activity_login_data_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        loginActivityViewModel.getSignInFragmentState().setPassword(password.getText());
        loginActivityViewModel.getSignInFragmentState().setEmail(email.getText());
    }

    @Override
    protected void initializeViews(View rootView) {
        email = rootView.findViewById(R.id.email);
        email.setRegex(Patterns.EMAIL_ADDRESS);
        email.setErrorLabel(getString(R.string.activity_login_incorrect_email));

        password = rootView.findViewById(R.id.password);
        password.setRegex(Pattern.compile("^.{4,}$"));
        password.setErrorLabel(getString(R.string.activity_login_incorrect_password));
        password.setAsLastField();

        Button submit = rootView.findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
    }
}
