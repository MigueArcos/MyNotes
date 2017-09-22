package com.example.miguel.misnotas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private Button cambiar_fragmentos;
    private Fragment fragmento_l;
    private Fragment fragmento_r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*if (savedInstanceState != null) {
            //Restore the fragment's instance
            fragmento_l = getSupportFragmentManager().getFragment(savedInstanceState, "fragmento_login");
            //fragmento_r = getSupportFragmentManager().getFragment(savedInstanceState, "fragmento_registro");
        }else{
            fragmento_l=new fragmento_login();
            fragmento_r=new fragmento_registro();
        }*/
        fragmento_l=new fragmento_login();
        fragmento_r=new fragmento_registro();
        cambiar_fragmentos=(Button)findViewById(R.id.cambiar_fragmentos);
        cambiar_fragmentos.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenido, fragmento_l).commit();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "fragmento_login", fragmento_l);
        //getSupportFragmentManager().putFragment(outState, "fragmento_registro", fragmento_r);
    }
    @Override
    public void onClick(View v) {
        if (getSupportActionBar().getTitle().equals("Inicio De Sesión")){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido, fragmento_r).commit();
            getSupportActionBar().setTitle("Registro");
            cambiar_fragmentos.setText("Iniciar Sesión");
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido, fragmento_l).commit();
            getSupportActionBar().setTitle("Inicio De Sesión");
            cambiar_fragmentos.setText("Crear una cuenta");
        }
    }
}
