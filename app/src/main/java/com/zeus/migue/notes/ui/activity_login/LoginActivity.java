package com.zeus.migue.notes.ui.activity_login;

import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.ui.activity_login.fragments.sign_in.SignInFragment;
import com.zeus.migue.notes.ui.activity_login.fragments.sign_up.SignUpFragment;
import com.zeus.migue.notes.ui.shared.BaseActivity;

public class LoginActivity extends BaseActivity {
    private Button switchFragments;
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        loginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        switchFragments = findViewById(R.id.switch_fragments_button);
        loadFragments();
        switchFragments.setOnClickListener(v -> {
            loginActivityViewModel.toggleFragments();
            loadFragments();
        });
    }

    private void loadFragments() {
        signInFragment = new SignInFragment();
        signUpFragment = new SignUpFragment();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (currentFragment != null){
            if (currentFragment instanceof SignInFragment){
                signInFragment = (SignInFragment) currentFragment;
                signUpFragment = new SignUpFragment();
            }else{
                signInFragment = new SignInFragment();
                signUpFragment = (SignUpFragment) currentFragment;
            }
        }
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
    @Override
    public void initializeViews() {

    }
}
