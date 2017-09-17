package com.example.miguel.misnotas;

/**
 * Created by Migue on 04/07/2017.
 */

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class fragmento_registro extends Fragment implements View.OnClickListener, View.OnFocusChangeListener{

    private TextInputLayout label_email, label_password, label_password_c, label_username;
    private EditText email, password, password_c, username;
    private Button submit;
    private Pattern regex_password;
    private AlertDialog mensaje;
    private Sincronizacion sync;
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
        sync=new Sincronizacion(getActivity());
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
            sync.Registrar(username.getText().toString(),email.getText().toString(),password.getText().toString());
            //Hacer registro
        }
        else{
            Toast.makeText(getActivity(), "Hay un error con los datos", Toast.LENGTH_SHORT).show();
        }

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
}

