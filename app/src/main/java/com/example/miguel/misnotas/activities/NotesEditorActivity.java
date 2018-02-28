package com.example.miguel.misnotas.activities;

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

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.R;

public class NotesEditorActivity extends AppCompatActivity implements TextWatcher{
    private TextView title, content;
    private AlertDialog mensaje;
    private Boolean NuevaNota;
    private int noteToModifyId,cambios=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);
        Bundle paquete = getIntent().getExtras();
        NuevaNota= paquete.getBoolean("NuevaNota",true);
        noteToModifyId = paquete.getInt("noteToModifyId",-1);
        title=(TextView)findViewById(R.id.title);
        content=(TextView)findViewById(R.id.content);
        title.setText(paquete.getString("title",""));
        content.setText(paquete.getString("content",""));
        if (Linkify.addLinks(content, Linkify.ALL)){
            content.append("\n");
        }
        title.addTextChangedListener(this);
        content.addTextChangedListener(this);
        mensaje = new AlertDialog.Builder(this).create();
        //Toast.makeText(getBaseContext(),"hola minmdo", Toast.LENGTH_SHORT).show();
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onStop() {
        super.onStop();
        InsertOrUpdate();
        //Toast.makeText(this, "Se ejecuto onStop de actividad", Toast.LENGTH_SHORT).show();
        //Se va a guardar la note si la bandera_guardar esta activa y ademas el campo de contenio o el de title ya tienen algo
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_notes_editor, menu);
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
        //This line is to ensure that this activity will come back to MainActivity (This is useful when this activity is called from search, because in this way is not necessary to remain the query to still showing the coincidences in search activity)
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        //this.finish();
    }


    void eliminar(){
        if (!(title.getText().toString().equals("") && content.getText().toString().equals(""))){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_note_confirmation).setCancelable(false);
            builder.setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Database.getInstance(NotesEditorActivity.this).eliminar_nota(noteToModifyId);
                    NotesEditorActivity.this.finish();
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
        noteToModifyId=Database.getInstance(NotesEditorActivity.this).guardar_nota(title.getText().toString(), content.getText().toString());
        NuevaNota=false;
        Toast.makeText(this.getBaseContext(), R.string.saved_label, Toast.LENGTH_SHORT).show();
        cambios=0;
    }
    void modificar(){
        Database.getInstance(NotesEditorActivity.this).modificar_nota(title.getText().toString(), content.getText().toString(),noteToModifyId);
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
