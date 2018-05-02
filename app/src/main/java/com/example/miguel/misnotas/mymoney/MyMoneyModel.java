package com.example.miguel.misnotas.mymoney;

import android.util.SparseBooleanArray;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.models.Finance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 79812 on 30/04/2018.
 */

public class MyMoneyModel implements MyMoneyMVP.Model<Finance> {
    private MyMoneyMVP.Presenter presenter;
    private List<Finance> data;

    public MyMoneyModel(MyMoneyMVP.Presenter presenter){
        this.presenter = presenter;
        data = new ArrayList<>();
    }

    @Override
    public void createFinance(String name, int value) {
        data.add(Database.getInstance().saveFinance(name, value));
    }

    @Override
    public void updateFinance(int position, String name, int value) {
        Database.getInstance().modificar(name, value, data.get(position).getidbase());
        data.get(position).setValor(value);
        data.get(position).setNombre(name);
        presenter.updateItem(position);
    }

    @Override
    public void deleteFinance(int position) {
        Database.getInstance().eliminarrecurso(data.get(position).getidbase());
        data.remove(position);
    }


    @Override
    public void bindViewAtPosition(int position, MyMoneyMVP.ItemView itemView) {
        itemView.setName(data.get(position).getNombre());
        itemView.setValue(data.get(position).getvalor());
        itemView.setIcon(data.get(position).getDrawableImageID());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    @Override
    public List<Finance> getSelectedItemsList(SparseBooleanArray selectedItemsIds) {
        final List<Finance> selectedItemList = new ArrayList<>();
        for (int i = 0; i < selectedItemsIds.size(); i++) {
            selectedItemList.add(data.get(selectedItemsIds.keyAt(i)));
        }
        return selectedItemList;
    }


    @Override
    public void requestFinances() {
        data = Database.getInstance().getFinances();
    }

    @Override
    public Finance getItemAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public int getTotal() {
        int total = 0;
        for (Finance finance : data){
            total += finance.getvalor();
        }
        return total;
    }

    @Override
    public String getLastUpdate() {
        return Database.getInstance().LastUpdate();
    }


}
