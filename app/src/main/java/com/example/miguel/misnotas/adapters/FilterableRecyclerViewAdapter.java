package com.example.miguel.misnotas.adapters;

import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

/**
 * Created by 79812 on 27/02/2018.
 */
public abstract class FilterableRecyclerViewAdapter<DataModel, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    protected List<DataModel> list;
    protected List<DataModel> originalList;

    public FilterableRecyclerViewAdapter(List<DataModel> list) {
        this.originalList = list;
        this.list = list;
    }

    public abstract void filterResults(String filter);
}