package com.zeus.migue.notes.ui.activity_main.fragments.notes;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.activity_main.fragments.notes.shared.BaseNotesFragment;

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
        if (swipeDir == ItemTouchHelper.RIGHT) {
            dismissSnackBar();
            snackbar = Snackbar.make(list, R.string.snackbar_generic_text, 10000).
                    setAction(R.string.snackbar_undo_text, view -> recoverDataAtPosition(data, position)).
                    addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event != DISMISS_EVENT_ACTION) {
                                data.setDeleted(true);
                                data.setModificationDate(Utils.toIso8601(new Date(), true));
                                viewModel.updateItem(data);
                            }
                        }
                        @Override
                        public void onShown(Snackbar snackbar) {
                            adapter.getItems().remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                    });
            snackbar.show();
        } else if (swipeDir == ItemTouchHelper.LEFT) {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_warning_title).setMessage(R.string.activity_main_fragment_deleted_notes_cannot_recover_notes_warning).setPositiveButton(R.string.dialog_ok_message, (dialog, which) -> viewModel.deleteItem(data)).setNegativeButton(R.string.dialog_cancel_message, (dialog, which) -> recoverDataAtPosition(data, position)).setCancelable(false).show();
            adapter.getItems().remove(position);
            adapter.notifyItemRemoved(position);
        }
    }
}
