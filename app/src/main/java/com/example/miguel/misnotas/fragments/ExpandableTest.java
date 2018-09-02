package com.example.miguel.misnotas.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.notes.NotesContract;
import com.example.miguel.misnotas.notes.current_notes.NotesAdapter;
import com.example.miguel.misnotas.notes.current_notes.NotesPresenter;

public class ExpandableTest extends Fragment implements NotesContract.View, NotesAdapter.AdapterActions{

    private CardView cardView;
    private LinearLayout expandedContent;
    private RecyclerView list;

    private NotesAdapter adapter;
    private NotesPresenter presenter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.expandable_test, container, false);
        cardView = rootView.findViewById(R.id.card_view);
        //expandedContent = rootView.findViewById(R.id.expandable);
        presenter = new NotesPresenter(this, false);
        list = rootView.findViewById(R.id.list);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        list.setHasFixedSize(true);

        adapter = new NotesAdapter(getContext());
        adapter.setListener(this);
        adapter.setPresenter(presenter);
        list.setAdapter(adapter);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(cardView);
                list.setVisibility(list.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        adapter.observeData();
        return rootView;
    }

    @Override
    public void onSwipe(int position) {

    }

    @Override
    public void onItemClick(View v, int position) {

    }

    @Override
    public void onLongClick(View v, int position) {

    }

    @Override
    public void onIconClick(View v, int position) {

    }

    @Override
    public void notifyItemCreated() {

    }

    @Override
    public void notifyItemCreated(int position) {

    }

    @Override
    public void notifyItemUpdated(int position) {

    }

    @Override
    public void notifyItemDeleted(int position) {

    }

    @Override
    public void notifyChanges() {

    }
}

