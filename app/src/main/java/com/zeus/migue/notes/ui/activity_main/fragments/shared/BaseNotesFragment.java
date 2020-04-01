package com.zeus.migue.notes.ui.activity_main.fragments.shared;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.activity_notes_editor.BottomSheetNotesEditor;
import com.zeus.migue.notes.ui.shared.BaseFragment;

import java.util.ArrayList;

public abstract class BaseNotesFragment extends BaseFragment {
    protected RecyclerView list;
    private TextView emptyListLabel;
    private SwipeRefreshLayout loader;
    private boolean isLoading = false;
    protected BaseNotesFragmentAdapter adapter;
    protected BaseNotesFragmentViewModel notesFragmentViewModel;
    private FloatingActionButton create;
    private SimpleSearchView searchView;
    protected Snackbar snackbar;
    private boolean showDeleted = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        showDeleted = args != null && args.getBoolean("showDeleted", false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_fragment_notes, container, false);
        initializeViews(rootView);
        //ViewModel observers
        if (getActivity() != null) {
            notesFragmentViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
                @NonNull
                @Override
                @SuppressWarnings("unchecked")
                public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                    return (T) new BaseNotesFragmentViewModel(getActivity().getApplication(), showDeleted);
                }
            }).get(BaseNotesFragmentViewModel.class);
            notesFragmentViewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
                emptyListLabel.setVisibility(notes.size() > 0 ? View.GONE : View.VISIBLE);
                adapter.updateData(notes);
                list.scrollToPosition(0);
            });
            notesFragmentViewModel.getEventData().observe(getViewLifecycleOwner(), liveDataEvent -> {
                Event event = liveDataEvent.getContentIfNotHandled();
                if (event != null) {
                    if (event.getMessageType() == Event.MessageType.SHOW_IN_DIALOG) {
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.generic_error_title).setMessage(event.getLocalResId()).show();
                    } else if (event.getMessageType() == Event.MessageType.SHOW_IN_TOAST) {
                        Toast.makeText(getActivity(), getString(event.getLocalResId()), Toast.LENGTH_LONG).show();
                    }
                }
            });
            searchView = getActivity().findViewById(R.id.searchView);
            setHasOptionsMenu(true);
            //notesFragmentViewModel.update();
        }

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            list.scrollToPosition(0);
        }
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
        adapter = new BaseNotesFragmentAdapter(showDeleted);
        adapter.setOnItemClick((data, position) -> {
            dismissSnackBar();
            BottomSheetNotesEditor dialog = new BottomSheetNotesEditor();
            Bundle packageData = new Bundle();
            packageData.putString("payload", new Gson().toJson(data));
            dialog.setArguments(packageData);
            dialog.show(getActivity().getSupportFragmentManager(), dialog.getTag());
        });

        adapter.setOnItemSwipe(this::handleItemSwipe);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        list.setLayoutManager(llm);
        list.setHasFixedSize(true);
        list.setAdapter(adapter);
        //setHasOptionsMenu(true);
    }

    protected void dismissSnackBar() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_fragment_notes, menu);
        setupSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupSearchView(Menu menu) {
        int EXTRA_REVEAL_CENTER_PADDING = 40;
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        // Adding padding to the animation because of the hidden menu item
        Point revealCenter = searchView.getRevealAnimationCenter();
        revealCenter.x -= DimensUtils.convertDpToPx(EXTRA_REVEAL_CENTER_PADDING, getActivity());
        searchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (Utils.stringIsNullOrEmpty(newText)) {
                    filterWithEmpty();
                } else {
                    notesFragmentViewModel.filterNotes(newText);
                }
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                filterWithEmpty();
                return false;
            }
        });
        searchView.setOnSearchViewListener(new SimpleSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                handleFab(false);
                dismissSnackBar();
            }

            @Override
            public void onSearchViewClosed() {
                handleFab(true);
                notesFragmentViewModel.filterNotes(Utils.EMPTY_STRING);
            }

            @Override
            public void onSearchViewShownAnimation() {

            }

            @Override
            public void onSearchViewClosedAnimation() {

            }
        });
    }

    private void filterWithEmpty() {
        adapter.updateData(new ArrayList<>());
        emptyListLabel.setVisibility(View.VISIBLE);
    }

    protected void recoverDataAtPosition(NoteDTO data, int position) {
        adapter.getNotes().add(position, data);
        adapter.notifyItemInserted(position);
    }

    private void handleFab(boolean isVisible){
        create.setVisibility(isVisible && !showDeleted ? View.VISIBLE : View.GONE);
    }

    public abstract void handleItemSwipe(NoteDTO dto, int position, int swipeDir);
}
