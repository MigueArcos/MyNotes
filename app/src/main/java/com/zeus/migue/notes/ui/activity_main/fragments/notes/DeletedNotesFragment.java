package com.zeus.migue.notes.ui.activity_main.fragments.notes;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.activity_main.fragments.shared.BaseNotesFragment;

import java.util.Date;

public class DeletedNotesFragment extends BaseNotesFragment {
    public static DeletedNotesFragment newInstance() {
        DeletedNotesFragment myFragment = new DeletedNotesFragment();
        Bundle args = new Bundle();
        args.putBoolean("showDeleted", true);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public void handleItemSwipe(NoteDTO data, int position, int swipeDir) {
        if (swipeDir == ItemTouchHelper.RIGHT){
            new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_info_title).setMessage(R.string.activity_main_fragment_deleted_notes_recover_note_text).setPositiveButton(R.string.dialog_ok_message, (dialog, which) -> {
                data.setDeleted(false);
                data.setModified(true);
                data.setModificationDate(Utils.toIso8601(new Date(), true));
                notesFragmentViewModel.updateNote(data);
            }).setNegativeButton(R.string.dialog_cancel_message, (dialog, which) -> {
                recoverDataAtPosition(data, position);
            }).show();
        }
        else if (swipeDir == ItemTouchHelper.LEFT){
            new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_warning_title).setMessage(R.string.activity_main_fragment_deleted_notes_cannot_recover_notes_warning).setPositiveButton(R.string.dialog_ok_message, (dialog, which) -> notesFragmentViewModel.deleteNote(data)).setNegativeButton(R.string.dialog_cancel_message, (dialog, which) -> {
                recoverDataAtPosition(data, position);
            }).show();
        }
        adapter.getNotes().remove(position);
        adapter.notifyItemRemoved(position);
    }
}
