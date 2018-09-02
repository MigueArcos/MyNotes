package com.example.miguel.misnotas.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.notes.deleted_notes.DeletedNotesFragment;
import com.example.miguel.misnotas.notes.current_notes.NotesFragment;


public class SearchNotesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static final int NOTES = 1;
    public static final int DELETED_NOTES = 2;
    private int type;
    private NotesFragment notesFragment;
    private DeletedNotesFragment deletedNotesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt("type");
            switch (type) {
                case NOTES:
                    notesFragment = new NotesFragment();
                    notesFragment.setArguments(getIntent().getExtras());
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, notesFragment, notesFragment.getClass().getSimpleName()).commit();
                    break;
                case DELETED_NOTES:
                    deletedNotesFragment = new DeletedNotesFragment();
                    deletedNotesFragment.setArguments(getIntent().getExtras());
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, deletedNotesFragment, deletedNotesFragment.getClass().getSimpleName()).commit();
                    break;
            }
        }
        setContentView(R.layout.activity_search_notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_search_notes, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint(getString(R.string.search_activity_search_view_label));
        searchView.setOnQueryTextListener(this);
        /**
         * These five lines are necessary to expand searchView by default and show the keyboard when this activity starts
         */
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.requestFocusFromTouch();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        switch (type) {
            case NOTES:
                notesFragment.filterNotes(newText);
                break;
            case DELETED_NOTES:
                deletedNotesFragment.filterNotes(newText);
                break;
        }
        return false;
    }
}
