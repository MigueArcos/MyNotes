package com.example.miguel.misnotas.adapters;

import android.support.v7.widget.RecyclerView;

import com.example.miguel.misnotas.utilities.MyFilterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 79812 on 27/02/2018.
 */
public abstract class FilterableRecyclerViewAdapter<DataModel extends MyFilterable, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    protected List<DataModel> data;
    protected List<DataModel> originalList;
    protected DataObserver dataObserver;


    public interface DataObserver {
        void onChanged(int listSize);
    }


    public interface ActionModeAdapterCallbacks<T> {
        void toggleSelection(int position);
        void clearSelections();
        int getSelectedCount();
        List<T> getSelectedItems();
    }


    public FilterableRecyclerViewAdapter() {}

    public void setDataObserver(DataObserver dataObserver){
        this.dataObserver = dataObserver;
    }

    public void loadDataSet(List<DataModel> data) {
        this.originalList = data;
        this.data = new ArrayList<>(data);

        if (dataObserver != null){
            dataObserver.onChanged(data.size());
        }
    }

    public void filterResults(String filter){
        List<DataModel> filteredResults = new ArrayList<>();
        if (filter.isEmpty()){
            updateDataSet(originalList);
            return;
        }
        for (DataModel dataModel : originalList){
            if (dataModel.passFilter(filter)){
                filteredResults.add(dataModel);
            }
        }
        updateDataSet(filteredResults);
    }

    //Sacado de aqui -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void updateDataSet(List<DataModel> data) {
        this.data = data;
        notifyDataSetChanged();
        if (dataObserver != null){
            dataObserver.onChanged(data.size());
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteItem(int position) {
        data.remove(position);
        originalList.remove(position);
        notifyItemRemoved(position);
        if (dataObserver != null){
            dataObserver.onChanged(data.size());
        }
    }


    public void insertItem(int position, DataModel dataModel) {
        data.add(position, dataModel);
        originalList.add(position, dataModel);
        notifyItemInserted(position);
        if (dataObserver != null){
            dataObserver.onChanged(data.size());
        }
    }

    //If position is not supplied then we will insert the item in the first place
    public void insertItem(DataModel dataModel) {
        data.add(0, dataModel);
        originalList.add(0, dataModel);
        notifyItemInserted(0);
        if (dataObserver != null){
            dataObserver.onChanged(data.size());
        }
    }

    public void modifyItem(int position, DataModel dataModel) {
        deleteItem(position);
        insertItem(dataModel);
    }

    public List<DataModel> getCurrentData() {
        return data;
    }
}