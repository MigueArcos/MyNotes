package com.example.miguel.misnotas.fragments;

import android.arch.lifecycle.ViewModelProviders;
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
import com.example.miguel.misnotas.models.Note;
import com.example.miguel.misnotas.viewmodels.DeletedNotesFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.miguel.misnotas.activities.SearchNotesActivity.DELETED_NOTES;


/**
 * Created by Miguel on 19/07/2017.
 */
public class DeletedNotesFragment extends Fragment implements DeletedNotesAdapter.AdapterActions {
    private RecyclerView list;
    private DeletedNotesAdapter adapter;
    private List<Note> data;
    private AlertDialog.Builder dialogDeleteNote;
    private AlertDialog.Builder dialogRecoverNote;
    private TextView emptyList;
    private boolean calledFromSearch;
    private String text = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deleted_notes, container, false);
        if (this.getArguments() != null) {
            calledFromSearch = getArguments().getBoolean("calledFromSearch", false);
        }
        ViewModelProviders.of(this).get(DeletedNotesFragmentViewModel.class);
        list = rootView.findViewById(R.id.lista);
        data = Database.getInstance(getActivity()).getNotes(true);
        adapter = new DeletedNotesAdapter(data, this);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                emptyList.setVisibility(data.size() > 0 ? View.GONE : View.VISIBLE);
                super.onChanged();
            }
        });
        list.setAdapter(adapter);

        dialogDeleteNote = new AlertDialog.Builder(getActivity());
        dialogDeleteNote.setTitle(R.string.dialog_default_title).setMessage(R.string.delete_note_completely);

        dialogRecoverNote = new AlertDialog.Builder(getActivity());
        dialogRecoverNote.setTitle(R.string.dialog_default_title).setMessage(getString(R.string.fragment_deleted_notes_recover_note));
        dialogRecoverNote.setNegativeButton(R.string.negative_button_label, (dialog, which) -> {/*Empty lambda body*/});

        emptyList = rootView.findViewById(R.id.emptyList);
        if (!calledFromSearch) {
            this.setHasOptionsMenu(true);
            emptyList.setText(R.string.empty_list_default_text);
        } else {
            emptyList.setText(R.string.empty_list_search_text);
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
        getActivity().startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        filterNotes(text);
        super.onResume();
        //Toast.makeText(this.getActivity(), "Se ejecuto onResume de fragmento", Toast.LENGTH_SHORT).show();
    }


    private void DeleteNoteCompletely(int noteID) {
        Database.getInstance(getActivity()).eliminar_nota_completamente(noteID);
    }

    private void CancelDeleteNote(int position, Note selectedNote) {
        data.add(position, selectedNote);
        adapter.notifyItemInserted(position);
        list.scrollToPosition(position);
    }

    private void RecoverNote(int position, Note selectedNote) {
        Database.getInstance(getActivity()).recuperar_nota(selectedNote.getNoteId());
        data.remove(position);
        adapter.notifyItemRemoved(position);
        //It doesn't matter if the item was or not expanded, it will remove it from expandedItems (this will avoid further problems with positions)
        adapter.notifyExpandedItemDeleted(position);
    }

    @Override
    public void onClick(int position) {
        dialogRecoverNote.setPositiveButton(R.string.positive_button_label, (dialog, which) -> RecoverNote(position, data.get(position))).show();
    }

    @Override
    public void onSwipe(final int position) {
        final Note noteToDelete = data.get(position);
        dialogDeleteNote
                .setPositiveButton(R.string.positive_button_label, (dialog, which) -> DeleteNoteCompletely(noteToDelete.getNoteId()))
                .setNegativeButton(R.string.negative_button_label, (dialog, which) -> CancelDeleteNote(position, noteToDelete))
                .setOnCancelListener(dialog -> CancelDeleteNote(position, noteToDelete))
                .show();
        data.remove(position);
        adapter.notifyItemRemoved(position);
        //It doesn't matter if the item was or not expanded, it will remove it from expandedItems (this will avoid further problems with positions)
        adapter.notifyExpandedItemDeleted(position);
    }

    public void filterNotes(String text) {
        adapter.filterResults(text);
    }


}


