package com.zeus.migue.notes.ui.activity_main.fragments.clipboard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.ClipNoteDTO;
import com.zeus.migue.notes.data.room.entities.ClipNote;
import com.zeus.migue.notes.ui.activity_notes_editor.BottomSheetNotesEditor;
import com.zeus.migue.notes.ui.shared.BaseListFragment;

public class ClipboardFragment extends BaseListFragment<ClipNote, ClipNoteDTO, ClipboardViewModel> {
    public static ClipboardFragment newInstance() {
        ClipboardFragment myFragment = new ClipboardFragment();
        return myFragment;
    }


    @Override
    public void handleItemSwipe(ClipNoteDTO dto, int position, int swipeDir) {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_warning_title).setMessage(R.string.activity_main_fragment_deleted_notes_cannot_recover_notes_warning).setPositiveButton(R.string.dialog_ok_message, (dialog, which) -> viewModel.deleteItem(dto)).setNegativeButton(R.string.dialog_cancel_message, (dialog, which) -> recoverDataAtPosition(dto, position)).setCancelable(false).show();
        adapter.getItems().remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public ClipboardViewModel initializeViewModel() {
        return new ViewModelProvider(this).get(ClipboardViewModel.class);
    }

    @Override
    public void observeItems() {
        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            emptyListLabel.setVisibility(items.size() > 0 ? View.GONE : View.VISIBLE);
            adapter.updateData(items);
            //list.scrollToPosition(0);
        });
    }

    @Override
    public ClipboardAdapter getAdapter() {
        return new ClipboardAdapter(this::handleItemSwipe);
    }

    @Override
    public int getViewResId() {
        return R.layout.activity_main_fragment_notes;
    }

    @Override
    public void onChangeSearchViewState(boolean isShown) {

    }

    @Override
    protected void initializeViews(View rootView) {
        emptyListLabel = rootView.findViewById(R.id.empty_list_label);
        loader = rootView.findViewById(R.id.loader);
        FloatingActionButton create = rootView.findViewById(R.id.create_new_fab);
        create.setVisibility(View.GONE);

        list = rootView.findViewById(R.id.list);
        adapter = getAdapter();
        adapter.setItemClickListener((data, position) -> {
            BottomSheetNotesEditor dialog = new BottomSheetNotesEditor();
            Bundle packageData = new Bundle();
            packageData.putString("payload", new Gson().toJson(data));
            packageData.putBoolean("isClipNote", true);
            dialog.setArguments(packageData);
            dialog.show(getActivity().getSupportFragmentManager(), dialog.getTag());
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        list.setLayoutManager(llm);
        list.setHasFixedSize(true);
        list.setAdapter(adapter);
        //setHasOptionsMenu(true);
    }
}