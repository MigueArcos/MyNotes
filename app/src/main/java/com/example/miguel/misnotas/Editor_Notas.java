package com.example.miguel.misnotas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Editor_Notas extends AppCompatActivity implements TextWatcher{
    private TextView titulo, contenido;
    private AlertDialog mensaje;
    private Boolean NuevaNota;
    private int id_nota_mod,cambios=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_notas);
        Bundle paquete = getIntent().getExtras();
        NuevaNota= paquete.getBoolean("NuevaNota",true);
        id_nota_mod = paquete.getInt("id_nota_mod",-1);
        titulo=(TextView)findViewById(R.id.titulo);
        contenido=(TextView)findViewById(R.id.contenido);
        titulo.setText(paquete.getString("titulo",""));
        contenido.setText(paquete.getString("contenido",""));
        if (Linkify.addLinks(contenido, Linkify.ALL)){
            contenido.append("\n");
        }
        titulo.addTextChangedListener(this);
        contenido.addTextChangedListener(this);
        mensaje = new AlertDialog.Builder(this).create();
        //Toast.makeText(getBaseContext(),"hola minmdo", Toast.LENGTH_SHORT).show();
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onStop() {
        super.onStop();
        InsertOrUpdate();
        //Toast.makeText(this, "Se ejecuto onStop de actividad", Toast.LENGTH_SHORT).show();
        //Se va a guardar la nota si la bandera_guardar esta activa y ademas el campo de contenio o el de titulo ya tienen algo
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        MenuItem boton_guardar=menu.findItem(R.id.guardar);
        MenuItem boton_eliminar=menu.findItem(R.id.eliminar);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.guardar:
                InsertOrUpdate();
                break;
            case R.id.eliminar:
                eliminar();
                break;
            //Esta es la flechita de back que se crea cuando una actividad es hija de otra
            case android.R.id.home:
                InsertOrUpdate();
                break;
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        InsertOrUpdate();
        //This line is to ensure that this activity will come back to Main Activity (This is useful when this activity is called from search, because in this way is not necessary to remain the query to still showing the coincidences in search activity)
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        //this.finish();
    }


    void eliminar(){
        if (!(titulo.getText().toString().equals("") && contenido.getText().toString().equals(""))){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_note_confirmation).setCancelable(false);
            builder.setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Database.getInstance(Editor_Notas.this).eliminar_nota(id_nota_mod);
                    Editor_Notas.this.finish();
                }
            });
            builder.setNegativeButton(R.string.negative_button_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else{
            mensaje.setMessage(getString(R.string.no_data_to_delete));
            mensaje.show();
        }

        //Toast.makeText(this.getBaseContext(), "¡Guardado!", Toast.LENGTH_LONG).show();
    }
    void guardar(){
        id_nota_mod=Database.getInstance(Editor_Notas.this).guardar_nota(titulo.getText().toString(), contenido.getText().toString());
        NuevaNota=false;
        Toast.makeText(this.getBaseContext(), R.string.saved_label, Toast.LENGTH_SHORT).show();
        cambios=0;
    }
    void modificar(){
        Database.getInstance(Editor_Notas.this).modificar_nota(titulo.getText().toString(), contenido.getText().toString(),id_nota_mod);
        Toast.makeText(this.getBaseContext(), getString(R.string.saved_label), Toast.LENGTH_SHORT).show();
        cambios=0;
    }
    void InsertOrUpdate(){
        if (NuevaNota && cambios>0) {
            guardar();
        }
        else if (cambios>0){
            modificar();
        }

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        cambios++;
        Linkify.addLinks(s, Linkify.ALL);
    }
}
