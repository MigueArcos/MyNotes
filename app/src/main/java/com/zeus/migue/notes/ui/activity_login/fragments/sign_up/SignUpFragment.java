package com.zeus.migue.notes.ui.activity_login.fragments.sign_up;

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

public class SignUpFragment extends BaseFragment implements View.OnClickListener {
    private InputField username, email, password, confirmPassword;
    private LoaderDialog loader;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_login_fragment_sign_up, container, false);
        initializeViews(rootView);

        if (getActivity() != null) {
            loginActivityViewModel = new ViewModelProvider(getActivity()).get(LoginActivityViewModel.class);
            loginActivityViewModel.getEvent().observe(getViewLifecycleOwner(), liveDataEvent -> {
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
            password.setText(loginActivityViewModel.getSignUpFragmentState().getPassword());
            email.setText(loginActivityViewModel.getSignUpFragmentState().getEmail());
            username.setText(loginActivityViewModel.getSignUpFragmentState().getUserName());
            confirmPassword.setText(loginActivityViewModel.getSignUpFragmentState().getConfirmedPassword());
        }
        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (email.textIsValid() && password.textIsValid() && username.textIsValid() && confirmPassword.textIsValid()) {
            loader = LoaderDialog.newInstance(getString(R.string.activity_login_fragment_sign_up_loader_label));
            loader.setCancelable(false);
            loader.show(getActivity().getSupportFragmentManager(), "dialog");
            loginActivityViewModel.signUp(email.getText(), username.getText(), password.getText(), confirmPassword.getText());
        } else {
            Toast.makeText(getActivity(), R.string.activity_login_data_error, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        loginActivityViewModel.getSignUpFragmentState().setPassword(password.getText());
        loginActivityViewModel.getSignUpFragmentState().setEmail(email.getText());
        loginActivityViewModel.getSignUpFragmentState().setUserName(username.getText());
        loginActivityViewModel.getSignUpFragmentState().setConfirmedPassword(confirmPassword.getText());
    }

    @Override
    protected void initializeViews(View rootView) {
        Pattern regexPassword = Pattern.compile("^.{4,}$");

        username = rootView.findViewById(R.id.username);
        username.setRegex(regexPassword);
        username.setErrorLabel(getString(R.string.activity_login_incorrect_username));

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
    }
}
