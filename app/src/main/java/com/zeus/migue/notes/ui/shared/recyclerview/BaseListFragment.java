package com.zeus.migue.notes.ui.shared.recyclerview;

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
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.room.entities.BaseEntity;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.contracts.IFilterable;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.BaseFragment;

import java.util.ArrayList;

public abstract class BaseListFragment<Entity extends BaseEntity, DTO extends IFilterable & IEntityConverter<Entity>, ViewModel extends BaseListViewModel<Entity, DTO>> extends BaseFragment {
    protected RecyclerView list;
    protected TextView emptyListLabel;
    protected SwipeRefreshLayout loader;
    protected boolean isLoading = false;
    protected GenericRecyclerViewAdapter<DTO, ?> adapter;
    protected ViewModel viewModel;
    private SimpleSearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        boolean hasOptionsMenu = args != null && args.getBoolean("hasOptionsMenu", true);
        setHasOptionsMenu(args == null || hasOptionsMenu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getViewResId(), container, false);
        initializeViews(rootView);
        loader.setOnRefreshListener(() -> {
            loader.setRefreshing(true);
            startSynchronization();
        });
        loader.setRefreshing(false);
        //ViewModel observers
        if (getActivity() != null) {
            viewModel = initializeViewModel();
            observeItems();
            viewModel.getEvent().observe(getViewLifecycleOwner(), liveDataEvent -> {
                Event event = liveDataEvent.getContentIfNotHandled();
                if (event != null) {
                    String message = event.getLocalResId() == 0 ? Utils.stringIsNullOrEmpty(event.getMessage()) ? getString(R.string.sync_error) : event.getMessage() : getString(event.getLocalResId());
                    if (event.getMessageType() == Event.MessageType.SHOW_IN_DIALOG) {
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.generic_error_title).setMessage(message).show();
                    } else if (event.getMessageType() == Event.MessageType.SHOW_IN_TOAST) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                }
            });
            viewModel.getSyncResponse().observe(getViewLifecycleOwner(), syncPayload -> {
                loader.setRefreshing(false);
            });
            searchView = getActivity().findViewById(R.id.searchView);
            //viewModel.update();
        }

        return rootView;
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
                onChangeSearchViewState(true);
                filterWithEmpty();
            }

            @Override
            public void onSearchViewClosed() {
                onChangeSearchViewState(false);
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

    protected void startSynchronization(){
        viewModel.startSynchronization();
    }

    public abstract void handleItemSwipe(DTO dto, int position, int swipeDir);

    public abstract ViewModel initializeViewModel();

    public abstract void observeItems();

    public abstract GenericRecyclerViewAdapter<DTO, ?> getAdapter();

    public abstract int getViewResId();

    public abstract void onChangeSearchViewState(boolean isShown);
}
