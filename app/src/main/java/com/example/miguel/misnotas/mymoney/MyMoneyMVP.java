package com.example.miguel.misnotas.mymoney;

import android.util.SparseBooleanArray;

import java.util.List;

/**
 * Created by 79812 on 30/04/2018.
 */

public class MyMoneyMVP {
    interface View {
        void notifyItemCreated();

        void notifyChanges();

        void notifyItemUpdated(int position);

        void notifyItemDeleted(int position);

        void showTotal(int total);

        void showLastUpdate(String lastUpdate);
    }

    interface Presenter<DataModel> {
        void onBindViewAtPosition(int position, ItemView itemView);

        int getItemCount();

        void onItemClick(int position, String name, int value);

        void onItemLongClick(int position);

        void onItemIconClick(int position);

        List<DataModel> getSelectedItemsList(SparseBooleanArray selectedItemsIds);

        void onPlusIconClick(String name, int value);

        void requestFinances();

        DataModel getItemAtPosition(int position);

        void updateItem(int position);

        void getTotal();

        void getLastUpdate();
    }

    interface Model<DataModel> {
        void createFinance(String name, int value);

        void updateFinance(int position, String name, int value);

        void deleteFinance(int position);

        void bindViewAtPosition(int position, ItemView itemView);

        int getItemCount();

        List<DataModel> getSelectedItemsList(SparseBooleanArray selectedItemsIds);

        void requestFinances();

        DataModel getItemAtPosition(int position);

        int getTotal();

        String getLastUpdate();
    }

    interface ItemView {
        void setName(String name);

        void setValue(int value);

        void setIcon(int iconResourceId);
    }
}
