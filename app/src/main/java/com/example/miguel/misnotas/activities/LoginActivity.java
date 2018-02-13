package com.example.miguel.misnotas.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.fragments.SignInFragment;
import com.example.miguel.misnotas.fragments.SignUpFragment;
import com.example.miguel.misnotas.viewmodels.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button cambiar_fragmentos;
    private Fragment fragmento_l;
    private Fragment fragmento_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*if (savedInstanceState != null) {
            //Restore the fragment's instance
            fragmento_l = getSupportFragmentManager().getFragment(savedInstanceState, "SignInFragment");
            //fragmento_r = getSupportFragmentManager().getFragment(savedInstanceState, "SignUpFragment");
        }else{
            fragmento_l=new SignInFragment();
            fragmento_r=new SignUpFragment();
        }*/
        fragmento_l = new SignInFragment();
        fragmento_r = new SignUpFragment();
        ViewModelProviders.of(this).get(LoginActivityViewModel.class);
        cambiar_fragmentos = (Button) findViewById(R.id.cambiar_fragmentos);
        cambiar_fragmentos.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenido, fragmento_l).commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "SignInFragment", fragmento_l);
        //getSupportFragmentManager().putFragment(outState, "SignUpFragment", fragmento_r);
    }

    @Override
    public void onClick(View v) {
        if (getSupportActionBar().getTitle().equals(getString(R.string.activity_login_sign_in_label))) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido, fragmento_r).commit();
            getSupportActionBar().setTitle(R.string.activity_login_sign_up_label);
            cambiar_fragmentos.setText(R.string.activity_login_sign_in_label);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido, fragmento_l).commit();
            getSupportActionBar().setTitle(R.string.activity_login_sign_in_label);
            cambiar_fragmentos.setText(R.string.activity_login_sign_up_label);
        }
    }
}
