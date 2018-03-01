package com.example.miguel.misnotas.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.miguel.misnotas.models.Note;

import java.util.Collections;
import java.util.List;

/**
 * Created by 79812 on 27/02/2018.
 */
public abstract class FilterableRecyclerViewAdapter<DataModel, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    protected List<DataModel> data;
    protected List<DataModel> originalList;

    public interface DeletedNotesAdapterActions {
        void onClick(int position);

        void onSwipe(int position);
    }

    public interface NotesAdapterActions {
        void onSwipe(int position);

        void onClick(int position);

        void onLongClick(int position);
    }

    public FilterableRecyclerViewAdapter(List<DataModel> data) {
        this.originalList = data;
        this.data = data;
    }

    public abstract void filterResults(String filter);

    //Sacado de aqui -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void setData(List<DataModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteItem(int position){
        data.remove(position);
        originalList.remove(position);
        notifyItemRemoved(position);
    }


    public void insertItem(int position, DataModel dataModel){
        data.add(position, dataModel);
        originalList.add(position, dataModel);
        notifyItemInserted(position);
    }

    //If position is not supplied then we will insert the item in the first place
    public void insertItem(DataModel dataModel){
        data.add(0, dataModel);
        originalList.add(0, dataModel);
        notifyItemInserted(0);
    }

    public void modifyItem(int position, DataModel dataModel){
        data.set(position, dataModel);
        originalList.set(position, dataModel);
        notifyItemChanged(position);
        Collections.swap(data, position, 0);
        Collections.swap(originalList, position, 0);
    }
}