package com.example.miguel.misnotas;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Miguel on 13/06/2016.
 */
public class fragmento_gastos extends Fragment implements View.OnClickListener,TextWatcher {
    private int tot = 0;
    private TextView array[]=new TextView[8];
    private AlertDialog mensaje;
    private Button boton;
    private SharedPreferences Semana;
    private SharedPreferences.Editor Editor;
    private int cambios=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragmento_gastos, container, false);
        Semana= this.getActivity().getSharedPreferences("Week",Context.MODE_PRIVATE);
        Editor=Semana.edit();
        mensaje = new AlertDialog.Builder(this.getActivity()).create();
        mensaje.setTitle(R.string.fragment_week_expenses_title);
        array[0]=(TextView) rootView.findViewById(R.id.eddomingo);
        array[1]=(TextView) rootView.findViewById(R.id.edlunes);
        array[2]=(TextView) rootView.findViewById(R.id.edmartes);
        array[3]=(TextView) rootView.findViewById(R.id.edmiercoles);
        array[4]=(TextView) rootView.findViewById(R.id.edjueves);
        array[5]=(TextView) rootView.findViewById(R.id.edviernes);
        array[6]=(TextView) rootView.findViewById(R.id.edsabado);
        array[7]=(TextView) rootView.findViewById(R.id.total);
        boton=(Button)rootView.findViewById(R.id.boton);
        boton.setOnClickListener(this);
        leer();
        for (int i=0; i<7; i++){
            array[i].addTextChangedListener(this);
        }
        return rootView;
    }

    void leer() {
        for (int i = 0; i < 7; i++) {
            array[i].setText(Semana.getString("Dia_" + (i + 1), ""));
        }
        //tot=Integer.parseInt(Semana.getString("Total","$ "));
        sumar();
        //array[7].setText(Semana.getString("Total", "$ "));
    }

    void sumar() {
        tot = 0;
        int arr[] = new int[7];
        for (int i = 0; i < 7; i++) {
            arr[i] = (!array[i].getText().toString().equals("")) ? Integer.parseInt(array[i].getText().toString()) : 0;
            tot += arr[i];
        }
        array[7].setText("$ " + tot);
    }

    @Override
    public void onClick(View v) {
        if (tot > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage(R.string.delete_all_confirmation).setCancelable(false);
            builder.setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    for (int i = 0; i < 7; i++) {
                        array[i].setText("");
                    }
                    tot = 0;
                }
            });
            builder.setNegativeButton(R.string.negative_button_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            mensaje.setMessage(getString(R.string.no_data_to_delete));
            mensaje.show();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        for (int i = 0; i < 7; i++) {
            Editor.putString("Dia_"+(i+1),array[i].getText().toString());
            //Editor.commit();
        }
        Editor.putString("Total",array[7].getText().toString());
        Editor.commit();
        if (cambios!=0){
            Toast.makeText(getActivity(), R.string.saved_label, Toast.LENGTH_SHORT).show();
            cambios=0;
        }

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        sumar();
        cambios++;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
