package com.example.miguel.misnotas.utilities;

import java.util.List;

/**
 * Created by 79812 on 19/07/2018.
 */

public class FilterableAdapterPresenter<DataModel extends MyFilterable> implements FilterableAdapterPresenterContract<DataModel> {
    private FilterableAdapterModelContract<DataModel> model;

    public FilterableAdapterPresenter(FilterableAdapterModelContract<DataModel> model) {
        this.model = model;
    }

    @Override
    public void filterResults(String filter) {
        model.filterResults(filter);
    }

    @Override
    public int getItemCount() {
        return model.getItemCount();
    }

    @Override
    public List<DataModel> getCurrentData() {
        return model.getCurrentData();
    }

    @Override
    public void addItem(DataModel item) {

    }

    @Override
    public void addItem(DataModel item, int position) {

    }

    @Override
    public void deleteItem(int position, boolean forever) {

    }

    @Override
    public void deleteItemForever(DataModel item) {

    }

    @Override
    public void updateItem(DataModel newItem, int position) {

    }

    @Override
    public void cancelItemDeletion(DataModel item, int position) {

    }

    @Override
    public DataModel getItem(int position) {
        return null;
    }

    @Override
    public void reloadData() {

    }
}
