package com.example.miguel.misnotas.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.MyUtils;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.models.Note;

public class NotesEditorActivity extends AppCompatActivity implements TextWatcher {
    private TextView title, content;
    private AlertDialog message;
    private boolean isNewNote;
    private int noteToModifyId, changesCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);
        Bundle packageData = getIntent().getExtras();
        assert packageData != null;
        isNewNote = packageData.getBoolean("isNewNote", true);
        noteToModifyId = packageData.getInt("noteToModifyId", -1);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        title.setText(packageData.getString("title", ""));
        content.setText(packageData.getString("content", ""));
        if (Linkify.addLinks(content, Linkify.ALL)) {
            content.append("\n");
        }
        title.addTextChangedListener(this);
        content.addTextChangedListener(this);
        message = new AlertDialog.Builder(this).create();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.save:
                InsertOrUpdate();
                break;
            case R.id.delete:
                delete();
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
        String noteTitle = title.getText().toString();
        String noteContent = content.getText().toString();
        if (!noteTitle.isEmpty() || !noteContent.isEmpty()) {
            //Log.d(MyUtils.GLOBAL_LOG_TAG, "TRying to find data");
            Intent returnIntent = getIntent();
            returnIntent.putExtra("resultNote", new Note(noteToModifyId, noteTitle, noteContent, MyUtils.formatDate(getString(R.string.date_format))));
            setResult(Activity.RESULT_OK, returnIntent);
        }
        else{
            setResult(Activity.RESULT_CANCELED);
        }
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
        //this.finish();
    }


    void delete() {
        if (!(title.getText().toString().equals("") && content.getText().toString().equals(""))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_note_confirmation).setCancelable(false);
            builder.setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Database.getInstance(NotesEditorActivity.this).deleteNote(noteToModifyId);
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
        } else {
            message.setMessage(getString(R.string.no_data_to_delete));
            message.show();
        }

        //Toast.makeText(this.getBaseContext(), "¡Guardado!", Toast.LENGTH_LONG).show();
    }

    void save() {
        noteToModifyId = Database.getInstance(NotesEditorActivity.this).saveNote(title.getText().toString(), content.getText().toString());
        isNewNote = false;
        Toast.makeText(this.getBaseContext(), R.string.saved_label, Toast.LENGTH_SHORT).show();
        changesCounter = 0;
    }

    void modify() {
        Database.getInstance(NotesEditorActivity.this).modifyNote(title.getText().toString(), content.getText().toString(), noteToModifyId);
        Toast.makeText(this.getBaseContext(), getString(R.string.saved_label), Toast.LENGTH_SHORT).show();
        changesCounter = 0;
    }

    void InsertOrUpdate() {
        if (isNewNote && changesCounter > 0) {
            save();
        } else if (changesCounter > 0) {
            modify();
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
        changesCounter++;
        Linkify.addLinks(s, Linkify.ALL);
    }
}
