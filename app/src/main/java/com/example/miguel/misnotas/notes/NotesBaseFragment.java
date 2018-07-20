package com.example.miguel.misnotas.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.models.Note;

import static com.example.miguel.misnotas.activities.SearchNotesActivity.DELETED_NOTES;

/**
 * Created by 79812 on 19/07/2018.
 */

public abstract class NotesBaseFragment extends Fragment {
    public static final int CALL_EDITOR_ACTIVITY = 1;
    public static final int CALL_SEARCH_ACTIVITY = 2;
    protected NotesContract.Presenter presenter;
    private int notesType;
    private boolean deletedNotes;
    protected RecyclerView list;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_notes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setPresenter(NotesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setNotesType(int notesType) {
        this.notesType = notesType;
        deletedNotes = notesType == DELETED_NOTES;
    }

    /*In this flow we have 3 activities: List, Search and Editor.
            List is capable to call Search or Editor
            Search is capable to call Editor
            Editor is not capable to call any activity

            Editor is the only activity capable to set OK as result
            Search is not capable to set result because it doesn't return anything, the only result this activity can give is the result of the editor activity when we forward the result of that activity or Activity.RESULT_CANCELED
         */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CALL_EDITOR_ACTIVITY:
                //In this case a new note was created or the resulting note know exactly which exactly its position in the adapter because the data hasn't been filtered yet
                if (resultCode == Activity.RESULT_OK) {
                    Note resultNote = data.getParcelableExtra("resultNote");
                    if (data.getBooleanExtra("isNewNote", true)) {
                        presenter.addItem(resultNote);
                    } else {
                        presenter.updateItem(resultNote, data.getIntExtra("position", -1));
                    }
                }
                break;
            case CALL_SEARCH_ACTIVITY:
                //Since we don't know the real position of the result when it return from searchActivity or because during the search activity all the data could have been modified, the best way to keep data consistency is to reload the data from database.
                updateFromDatabase();
                /*
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(getContext(), "OK as result code", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Not OK as result code", Toast.LENGTH_SHORT).show();
                }*/
                break;
        }
    }

    public void filterNotes(String text) {
        presenter.filterResults(text);
    }

    public void updateFromDatabase() {
        if (getView() == null) return;
        presenter.reloadData();
    }

}
