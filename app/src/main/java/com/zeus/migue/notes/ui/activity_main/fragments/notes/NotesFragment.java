package com.zeus.migue.notes.ui.activity_main.fragments.notes;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.activity_main.fragments.shared.BaseNotesFragment;

import java.util.Date;

public class NotesFragment extends BaseNotesFragment {
    public static NotesFragment newInstance() {
        NotesFragment myFragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putBoolean("showDeleted", false);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void handleItemSwipe(NoteDTO data, int position, int swipeDir) {
        dismissSnackBar();
        snackbar = Snackbar.make(list, R.string.snackbar_generic_text, 4000).
                setAction(R.string.snackbar_undo_text, view -> {
                    recoverDataAtPosition(data, position);
                }).
                addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event != DISMISS_EVENT_ACTION) {
                            data.setDeleted(true);
                            data.setModified(true);
                            data.setModificationDate(Utils.toIso8601(new Date(), true));
                            notesFragmentViewModel.updateNote(data);
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        adapter.getNotes().remove(position);
                        adapter.notifyItemRemoved(position);
                    }

                });

        snackbar.show();
    }
}
