package com.example.miguel.misnotas.mymoney;

import android.util.SparseBooleanArray;

import com.example.miguel.misnotas.models.Finance;

import java.util.List;

/**
 * Created by 79812 on 30/04/2018.
 */

public class MyMoneyPresenter implements MyMoneyMVP.Presenter<Finance> {
    private MyMoneyMVP.View view;
    private MyMoneyModel model;


    public MyMoneyPresenter(MyMoneyMVP.View view){
        this.view = view;
        model = new MyMoneyModel(this);

    }

    @Override
    public void onBindViewAtPosition(int position, MyMoneyMVP.ItemView itemView) {
        model.bindViewAtPosition(position, itemView);
    }

    @Override
    public int getItemCount() {
        return model.getItemCount();
    }

    @Override
    public void onItemClick(int position, String name, int value) {
        model.updateFinance(position, name, value);
    }

    @Override
    public void onItemLongClick(int position) {
        model.deleteFinance(position);
        view.notifyItemDeleted(position);
    }


    @Override
    public void onItemIconClick(int position) {

    }

    @Override
    public List<Finance> getSelectedItemsList(SparseBooleanArray selectedItemsIds) {
        return model.getSelectedItemsList(selectedItemsIds);
    }

    @Override
    public void onPlusIconClick(String name, int value) {
        model.createFinance(name, value);
        view.notifyItemCreated();
    }



    @Override
    public void requestFinances() {
        model.requestFinances();
        view.notifyChanges();
    }

    @Override
    public Finance getItemAtPosition(int position) {
        return model.getItemAtPosition(position);
    }

    @Override
    public void updateItem(int position) {
        view.notifyItemUpdated(position);
    }

    @Override
    public void getTotal() {
        view.showTotal(model.getTotal());
    }

    @Override
    public void getLastUpdate() {
        view.showLastUpdate(model.getLastUpdate());
    }

}
