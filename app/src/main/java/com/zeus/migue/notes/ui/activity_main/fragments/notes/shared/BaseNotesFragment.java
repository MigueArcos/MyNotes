package com.zeus.migue.notes.ui.activity_main.fragments.notes.shared;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.ui.activity_main.fragments.notes.NotesViewModel;
import com.zeus.migue.notes.ui.activity_notes_editor.BottomSheetNotesEditor;
import com.zeus.migue.notes.ui.shared.recyclerview.BaseListFragment;

public abstract class BaseNotesFragment extends BaseListFragment<Note, NoteDTO, NotesViewModel> {
    protected Snackbar snackbar;
    private boolean showDeleted = false;
    protected FloatingActionButton create;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        showDeleted = args != null && args.getBoolean("showDeleted", false);
    }

    @Override
    protected void initializeViews(View rootView) {
        emptyListLabel = rootView.findViewById(R.id.empty_list_label);
        loader = rootView.findViewById(R.id.loader);
        create = rootView.findViewById(R.id.create_new_fab);
        if (showDeleted){
            create.setVisibility(View.GONE);
        }else{
            create.setOnClickListener(v -> {
                dismissSnackBar();
                BottomSheetNotesEditor dialog = new BottomSheetNotesEditor();
                dialog.show(getActivity().getSupportFragmentManager(), dialog.getTag());
            });
        }

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
    @Override
    public NotesViewModel initializeViewModel() {
        return new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            @SuppressWarnings("unchecked")
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new NotesViewModel(getActivity().getApplication(), showDeleted);
            }
        }).get(NotesViewModel.class);
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
    public BaseNotesFragmentAdapter getAdapter() {
        return new BaseNotesFragmentAdapter(showDeleted, this::handleItemSwipe);
    }

    protected void dismissSnackBar() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public int getViewResId() {
        return R.layout.activity_main_fragment_notes;
    }

    @Override
    public void onChangeSearchViewState(boolean isShown) {
        dismissSnackBar();
        create.setVisibility(isShown ? View.GONE : showDeleted ? View.GONE : View.VISIBLE);
    }
}
