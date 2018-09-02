package com.example.miguel.misnotas.utilities;

import java.util.List;

/**
 * Created by 79812 on 18/07/2018.
 */

public interface FilterableAdapterModelContract<DataModel extends MyFilterable> {
    void filterResults(String filter);

    int getItemCount();

    List<DataModel> getCurrentData();

    void addItem(DataModel item);

    void addItem(DataModel item, int position);

    void deleteItem(int position, boolean forever);

    void deleteItemForever(DataModel item);

    void updateItem(DataModel newItem, int position);

    void cancelItemDeletion(DataModel item, int position);

    DataModel getItem(int position);

    void loadFromDataSource(List<DataModel> dataSource);
}