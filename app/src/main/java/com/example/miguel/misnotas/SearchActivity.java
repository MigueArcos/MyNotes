package com.example.miguel.misnotas;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;


public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static final int NOTES = 1;
    public static final int DELETED_NOTES = 2;
    private int type;
    private fragmento_notas fragmento_notas;
    private fragmento_notas_eliminadas fragmento_notas_eliminadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt("type");
            switch (type) {
                case NOTES:
                    fragmento_notas = new fragmento_notas();
                    fragmento_notas.setArguments(getIntent().getExtras());
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmento_notas, fragmento_notas.getClass().getSimpleName()).commit();
                    break;
                case DELETED_NOTES:
                    fragmento_notas_eliminadas = new fragmento_notas_eliminadas();
                    fragmento_notas_eliminadas.setArguments(getIntent().getExtras());
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmento_notas_eliminadas, fragmento_notas_eliminadas.getClass().getSimpleName()).commit();
                    break;
            }
        }
        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_search, menu);
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
                fragmento_notas.filterNotes(newText);
                break;
            case DELETED_NOTES:
                fragmento_notas_eliminadas.filterNotes(newText);
                break;
        }
        return false;
    }
}
