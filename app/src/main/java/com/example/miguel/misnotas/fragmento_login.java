package com.example.miguel.misnotas;

/**
 * Created by Migue on 04/07/2017.
 */

import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class fragmento_login extends Fragment implements View.OnClickListener, View.OnFocusChangeListener{

    private TextInputLayout label_email, label_password;
    private EditText email, password;
    private Button submit;
    private Pattern regex_password;
    private Bundle paquete=null;
    private Sincronizacion sync;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragmento_login, container, false);
        label_email=(TextInputLayout)rootView.findViewById(R.id.label_email);
        label_password=(TextInputLayout) rootView.findViewById(R.id.label_password);
        email=(EditText) rootView.findViewById(R.id.email);
        password=(EditText) rootView.findViewById(R.id.password);
        submit=(Button) rootView.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        regex_password=regex_password.compile("^.{4,}$");
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                        if (!ValidarPassword()){
                            label_password.setError("La contraseña es incorrecta, debe tener al menos 4 caracteres");
                        }
                        else{
                            label_password.setError(null);
                            label_password.setErrorEnabled(false);
                        }

                }
                return false; // pass on to other listeners.
            }
        });
        sync=new Sincronizacion(getActivity());
        return rootView;
    }

    boolean ValidarEmail(){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches();
    }
    boolean ValidarPassword(){
        return regex_password.matcher(password.getText()).matches();
    }
    @Override
    public void onClick(View v) {

        if (ValidarEmail() && ValidarPassword()){
            sync.IniciarSesion(email.getText().toString(), password.getText().toString());
        }
        else{
            Toast.makeText(getActivity(), "Hay un error con los datos", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus){
            switch (v.getId()){
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
                    if (!ValidarPassword()){
                        label_password.setError("La contraseña es incorrecta, debe tener al menos 4 caracteres");
                    }
                    else{
                        label_password.setError(null);
                        label_password.setErrorEnabled(false);
                    }
                    break;
            }
        }
    }
}

