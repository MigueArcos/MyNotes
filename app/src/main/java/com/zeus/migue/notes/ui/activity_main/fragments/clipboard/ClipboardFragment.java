package com.zeus.migue.notes.ui.activity_main.fragments.clipboard;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.ClipItemDTO;
import com.zeus.migue.notes.data.room.entities.ClipItem;
import com.zeus.migue.notes.ui.shared.BaseListFragment;
import com.zeus.migue.notes.ui.activity_notes_editor.BottomSheetNotesEditor;
import com.zeus.migue.notes.ui.shared.recyclerview.GenericRecyclerViewAdapter;

public class ClipboardFragment extends BaseListFragment<ClipItem, ClipItemDTO, ClipboardViewModel> {
    public static ClipboardFragment newInstance() {
        ClipboardFragment myFragment = new ClipboardFragment();
        return myFragment;
    }


    @Override
    public void handleItemSwipe(ClipItemDTO clipItemDTO, int position, int swipeDir) {

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
            list.scrollToPosition(0);
        });
    }

    @Override
    public GenericRecyclerViewAdapter<ClipItemDTO, ?> getAdapter() {
        return new ClipboardAdapter(this::handleItemSwipe);
    }

    @Override
    protected void initializeViews(View rootView) {
        emptyListLabel = rootView.findViewById(R.id.empty_list_label);
        loader = rootView.findViewById(R.id.loader);
        loader.setOnRefreshListener(() -> {
            if (isLoading) {
                loader.setRefreshing(true);
            }
        });
        loader.setRefreshing(false);

        create = rootView.findViewById(R.id.create_new_fab);
        create.setVisibility(View.GONE);

        list = rootView.findViewById(R.id.list);
        adapter = getAdapter();
        adapter.setItemClickListener((data, position) -> {
            dismissSnackBar();
            BottomSheetNotesEditor dialog = new BottomSheetNotesEditor();
            Bundle packageData = new Bundle();
            packageData.putString("payload", new Gson().toJson(data));
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