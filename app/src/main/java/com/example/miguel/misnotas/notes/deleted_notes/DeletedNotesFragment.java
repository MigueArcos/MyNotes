package com.example.miguel.misnotas.notes.deleted_notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.activities.SearchNotesActivity;
import com.example.miguel.misnotas.models.Note;
import com.example.miguel.misnotas.notes.NotesBaseFragment;
import com.example.miguel.misnotas.notes.NotesContract;

import static com.example.miguel.misnotas.activities.SearchNotesActivity.DELETED_NOTES;


/**
 * Created by Miguel on 19/07/2017.
 */
public class DeletedNotesFragment extends NotesBaseFragment implements DeletedNotesAdapter.AdapterActions, NotesContract.View{
    private RecyclerView list;
    private DeletedNotesAdapter adapter;
    private AlertDialog.Builder dialogDeleteNote;
    private AlertDialog.Builder dialogRecoverNote;
    private TextView emptyListLabel;
    private boolean calledFromSearch;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deleted_notes, container, false);
        Database.getInstance(getContext());
        presenter = new DeletedNotesPresenter(this, true);

        list = rootView.findViewById(R.id.list);
        emptyListLabel = rootView.findViewById(R.id.empty_list_label);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        list.setHasFixedSize(true);

        adapter = new DeletedNotesAdapter(getContext());

        adapter.setDataObserver(listSize -> {
            emptyListLabel.setVisibility(listSize == 0 ? View.VISIBLE : View.GONE);
        });

        adapter.setListener(this);

        adapter.setPresenter(presenter);

        list.setAdapter(adapter);

        dialogDeleteNote = new AlertDialog.Builder(getActivity());
        dialogDeleteNote.setTitle(R.string.dialog_default_title).setMessage(R.string.delete_note_completely);

        dialogRecoverNote = new AlertDialog.Builder(getActivity());
        dialogRecoverNote.setTitle(R.string.dialog_default_title).setMessage(getString(R.string.fragment_deleted_notes_recover_note));
        dialogRecoverNote.setNegativeButton(R.string.negative_button_label, (dialog, which) -> {/*Empty lambda body*/});


        if (this.getArguments() != null) {
            calledFromSearch = getArguments().getBoolean("calledFromSearch", false);
        }

        if (!calledFromSearch) {
            this.setHasOptionsMenu(true);
            emptyListLabel.setText(R.string.empty_list_default_text);
            //presenter.reloadData();
        } else {
            emptyListLabel.setText(R.string.empty_list_search_text);
            presenter.filterResults("");
        }

        adapter.observeData();
        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getActivity(), SearchNotesActivity.class);
        intent.putExtra("calledFromSearch", true);
        intent.putExtra("type", DELETED_NOTES);
        startActivityForResult(intent, CALL_SEARCH_ACTIVITY);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {
        dialogRecoverNote.setPositiveButton(R.string.positive_button_label,
                (dialog, which) -> presenter.onItemClick(position)).show();

    }

    @Override
    public void onSwipe(final int position) {
        final Note noteToDelete = presenter.getItem(position);
        presenter.onItemSwiped(position);
        dialogDeleteNote
                .setPositiveButton(R.string.positive_button_label, (dialog, which) -> {
                    presenter.deleteNote(noteToDelete.getNoteId(), true);
                    presenter.deleteItemForever(noteToDelete);
                })
                .setNegativeButton(R.string.negative_button_label, (dialog, which) -> presenter.cancelItemDeletion(noteToDelete, position))
                .setOnCancelListener(dialog -> presenter.cancelItemDeletion(noteToDelete, position))
                .show();
        //notifyItemDeleted(position);
    }

    @Override
    public void onArrowClick(int position) {
        /*
        BottomSheetFragment bottomSheetDialogFragment = new BottomSheetFragment();
        Bundle selectedOne = new Bundle();
        selectedOne.putParcelable("selectedNote", presenter.getItem(position));
        bottomSheetDialogFragment.setArguments(selectedOne);
        bottomSheetDialogFragment.setDialogResultListener(new BottomSheetFragment.DialogResultListener() {
            @Override
            public void onResultReady(Note tResult) {
                //Toast.makeText(getActivity(), "New Title:" +tResult.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        */
    }

    @Override
    public void notifyItemCreated() {
        adapter.notifyItemInserted(0);
    }

    @Override
    public void notifyItemCreated(int position) {
        adapter.notifyItemInserted(position);
        list.scrollToPosition(position);
    }

    @Override
    public void notifyItemUpdated(int position) {
        adapter.notifyItemChanged(position);
        list.scrollToPosition(position);
    }

    @Override
    public void notifyItemDeleted(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyChanges() {
        adapter.notifyDataSetChanged();
        adapter.observeData();
    }
}


