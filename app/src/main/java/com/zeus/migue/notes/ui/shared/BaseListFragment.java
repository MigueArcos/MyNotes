package com.zeus.migue.notes.ui.shared;

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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.room.entities.BaseEntity;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.contracts.IFilterable;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.recyclerview.GenericRecyclerViewAdapter;

import java.util.ArrayList;

public abstract class BaseListFragment<Entity extends BaseEntity, DTO extends IFilterable & IEntityConverter<Entity>, ViewModel extends BaseListViewModel<Entity, DTO>> extends BaseFragment {
    protected RecyclerView list;
    protected TextView emptyListLabel;
    protected SwipeRefreshLayout loader;
    protected boolean isLoading = false;
    protected GenericRecyclerViewAdapter<DTO, ?> adapter;
    protected ViewModel viewModel;
    private SimpleSearchView searchView;
    private Snackbar snackbar;
    protected FloatingActionButton create;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_base_list_fragment, container, false);
        initializeViews(rootView);
        //ViewModel observers
        if (getActivity() != null) {
            viewModel = initializeViewModel();
            observeItems();
            viewModel.getEventData().observe(getViewLifecycleOwner(), liveDataEvent -> {
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
            //viewModel.update();
        }

        return rootView;
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
                    viewModel.filterNotes(newText);
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
                filterWithEmpty();
            }

            @Override
            public void onSearchViewClosed() {
                handleFab(true);
                viewModel.filterNotes(Utils.EMPTY_STRING);
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

    protected void recoverDataAtPosition(DTO data, int position) {
        adapter.getItems().add(position, data);
        adapter.notifyItemInserted(position);
    }

    private void handleFab(boolean isVisible) {
        create.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public abstract void handleItemSwipe(DTO dto, int position, int swipeDir);

    public abstract ViewModel initializeViewModel();

    public abstract void observeItems();

    public abstract GenericRecyclerViewAdapter<DTO, ?> getAdapter();
}
