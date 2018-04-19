package com.example.miguel.misnotas.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.activities.SearchNotesActivity;
import com.example.miguel.misnotas.adapters.DeletedNotesAdapter;
import com.example.miguel.misnotas.adapters.FilterableRecyclerViewAdapter;
import com.example.miguel.misnotas.models.Note;

import java.util.List;

import static com.example.miguel.misnotas.activities.SearchNotesActivity.DELETED_NOTES;


/**
 * Created by Miguel on 19/07/2017.
 */
public class DeletedNotesFragment extends Fragment implements FilterableRecyclerViewAdapter.DeletedNotesAdapterActions {
    private RecyclerView list;
    private DeletedNotesAdapter adapter;
    private List<Note> data;
    private AlertDialog.Builder dialogDeleteNote;
    private AlertDialog.Builder dialogRecoverNote;
    private TextView emptyListLabel;
    private boolean calledFromSearch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deleted_notes, container, false);
        if (this.getArguments() != null) {
            calledFromSearch = getArguments().getBoolean("calledFromSearch", false);
        }
        list = rootView.findViewById(R.id.list);
        emptyListLabel = rootView.findViewById(R.id.empty_list_label);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        list.setHasFixedSize(true);

        data = Database.getInstance(getActivity()).getNotes(true);

        adapter = new DeletedNotesAdapter(this, getContext());

        adapter.setDataObserver(listSize -> emptyListLabel.setVisibility(listSize == 0 ? View.VISIBLE : View.GONE));

        adapter.loadData(data);
        list.setAdapter(adapter);

        dialogDeleteNote = new AlertDialog.Builder(getActivity());
        dialogDeleteNote.setTitle(R.string.dialog_default_title).setMessage(R.string.delete_note_completely);

        dialogRecoverNote = new AlertDialog.Builder(getActivity());
        dialogRecoverNote.setTitle(R.string.dialog_default_title).setMessage(getString(R.string.fragment_deleted_notes_recover_note));
        dialogRecoverNote.setNegativeButton(R.string.negative_button_label, (dialog, which) -> {/*Empty lambda body*/});

        if (!calledFromSearch) {
            this.setHasOptionsMenu(true);
            emptyListLabel.setText(R.string.empty_list_default_text);
        } else {
            emptyListLabel.setText(R.string.empty_list_search_text);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_notes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getActivity(), SearchNotesActivity.class);
        intent.putExtra("calledFromSearch", true);
        intent.putExtra("type", DELETED_NOTES);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        //filterNotes(text);
        super.onResume();
        //Toast.makeText(this.getActivity(), "Se ejecuto onResume de fragmento", Toast.LENGTH_SHORT).show();
    }


    private void DeleteNoteCompletely(int noteID) {
        Database.getInstance(getActivity()).deleteNoteCompletely(noteID);
    }

    private void CancelDeleteNote(int position, Note selectedNote) {
        adapter.insertItem(position, selectedNote);
        list.scrollToPosition(position);
    }

    private void RecoverNote(int position, Note selectedNote) {
        Database.getInstance(getActivity()).recoverNote(selectedNote.getNoteId());
        adapter.deleteItem(position);
        //It doesn't matter if the item was or not expanded, it will remove it from expandedItems (this will avoid further problems with positions)
        adapter.notifyExpandedItemDeleted(position);
    }

    @Override
    public void onItemClick(int position) {
        dialogRecoverNote.setPositiveButton(R.string.positive_button_label, (dialog, which) -> RecoverNote(position, adapter.getCurrentData().get(position))).show();
    }

    @Override
    public void onSwipe(final int position) {
        final Note noteToDelete = adapter.getCurrentData().get(position);
        dialogDeleteNote
                .setPositiveButton(R.string.positive_button_label, (dialog, which) -> DeleteNoteCompletely(noteToDelete.getNoteId()))
                .setNegativeButton(R.string.negative_button_label, (dialog, which) -> CancelDeleteNote(position, noteToDelete))
                .setOnCancelListener(dialog -> CancelDeleteNote(position, noteToDelete))
                .show();
        adapter.deleteItem(position);
        //It doesn't matter if the item was or not expanded, it will remove it from expandedItems (this will avoid further problems with positions)
        adapter.notifyExpandedItemDeleted(position);
    }

    public void filterNotes(String text) {
        adapter.filterResults(text);
    }

    public void updateFromDatabase(){
        if (adapter == null) return;
        adapter.loadData(Database.getInstance(getActivity()).getNotes(true));
        adapter.notifyDataSetChanged();
    }

}


