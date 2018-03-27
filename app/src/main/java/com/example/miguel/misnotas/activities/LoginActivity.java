package com.example.miguel.misnotas.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.fragments.SignInFragment;
import com.example.miguel.misnotas.fragments.SignUpFragment;
import com.example.miguel.misnotas.viewmodels.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity {
    private Button switchFragments;
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInFragment = new SignInFragment();
        signUpFragment = new SignUpFragment();
        loginActivityViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);
        switchFragments = findViewById(R.id.switch_fragments_button);
        loadFragments();
        switchFragments.setOnClickListener(v -> {
            loginActivityViewModel.toggleFragments();
            loadFragments();
        });
    }

    private void loadFragments() {
        if (loginActivityViewModel.isSignInFragment()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, signInFragment).commit();
            getSupportActionBar().setTitle(R.string.activity_login_sign_in_label);
            switchFragments.setText(R.string.activity_login_sign_up_label);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, signUpFragment).commit();
            getSupportActionBar().setTitle(R.string.activity_login_sign_up_label);
            switchFragments.setText(R.string.activity_login_sign_in_label);
        }
    }
}
