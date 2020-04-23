package com.zeus.migue.notes.ui.activity_notes_editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.BaseActivity;

import java.util.Date;

public class NotesEditorActivity extends BaseActivity  {
    private TextView title, content;
    private boolean isNewNote = true;
    private int changesCounter = 0;
    private String originalTitle, originalContent;
    private NotesEditorViewModel notesEditorViewModel;
    private NoteDTO note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);
        Bundle packageData = getIntent().getExtras();
        initializeViews();
        if (packageData != null && packageData.getBoolean("isNewNote", true)) {
            isNewNote = false;
            originalTitle = packageData.getString("title", "");
            originalContent = packageData.getString("content", "");
            title.setText(originalTitle);
            content.setText(originalContent);
        }

        if (Linkify.addLinks(content, Linkify.ALL)) {
            content.append("\n");
            originalContent += "\n";
        }

        notesEditorViewModel = new ViewModelProvider(this).get(NotesEditorViewModel.class);
        notesEditorViewModel.getEvent().observe(this, liveDataEvent -> {
            Event event = liveDataEvent.getContentIfNotHandled();
            if (event != null) {
                if (event.getMessageType() == Event.MessageType.SHOW_IN_DIALOG) {
                    new androidx.appcompat.app.AlertDialog.Builder(this).setTitle(R.string.generic_error_title).setMessage(event.getLocalResId()).show();
                } else if (event.getMessageType() == Event.MessageType.SHOW_IN_TOAST) {
                    Toast.makeText(this, getString(event.getLocalResId()), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_notes_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                insertOrUpdate();
                break;
            case R.id.delete:
                break;
            //Back arrow android
            case android.R.id.home:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (thereAreChanges()) {
            //Log.d(MyUtils.GLOBAL_LOG_TAG, "Trying to find data");
            insertOrUpdate();
            Intent returnIntent = getIntent();
            setResult(Activity.RESULT_OK, returnIntent);
        }
        else{
            setResult(Activity.RESULT_CANCELED);
        }
        super.onBackPressed();
    }

    @Override
    public void initializeViews() {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        TextWatcher textWatcher = new TextWatcher() {
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
        };
        title.addTextChangedListener(textWatcher);
        content.addTextChangedListener(textWatcher);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void insertOrUpdate() {
        String currentTitle = title.getText().toString(), currentContent = content.getText().toString();
        String date = Utils.toIso8601(new Date(), true);
        if (isNewNote && changesCounter > 0) {
            note = new NoteDTO(currentTitle, currentContent, date, date, false);
            notesEditorViewModel.insertNote(note);
        } else if (changesCounter > 0) {
            note.setTitle(currentTitle);
            note.setContent(currentContent);
            note.setModificationDate(date);
            notesEditorViewModel.updateNote(note);
        }
        changesCounter = 0;
    }
    private boolean thereAreChanges(){
        String currentTitle = title.getText().toString(), currentContent = content.getText().toString();
        return (!currentTitle.equals(originalTitle) || !currentContent.equals(originalContent)) && (!Utils.stringIsNullOrEmpty(currentTitle) || !Utils.stringIsNullOrEmpty(currentContent));
    }
}
