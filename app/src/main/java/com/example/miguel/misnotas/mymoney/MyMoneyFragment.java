package com.example.miguel.misnotas.mymoney;

/**
 * Created by Miguel on 13/06/2016.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miguel.misnotas.MyUtils;
import com.example.miguel.misnotas.R;

import java.util.Locale;

public class MyMoneyFragment extends Fragment implements MenuItem.OnMenuItemClickListener, ActionMode.Callback, MyMoneyMVP.View {
    private RecyclerView list;
    private MyMoneyAdapter adapter;
    private TextView totalText, lastUpdateText;
    private FinanceDialog financeDialog;
    private ActionMode actionMode;
    private MyMoneyPresenter presenter;
    private AlertDialog.Builder message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_money, container, false);
        presenter = new MyMoneyPresenter(this);

        totalText = rootView.findViewById(R.id.total);
        lastUpdateText = rootView.findViewById(R.id.last);
        list = rootView.findViewById(R.id.list);

        adapter = new MyMoneyAdapter(presenter, getContext());
        adapter.setFinancesAdapterActions(new MyMoneyAdapter.MyMoneyAdapterActions() {
            @Override
            public void onItemClick(View v, int position) {
                if (actionMode != null){
                    v.findViewById(R.id.foto).performClick();
                    updateActionMode();
                    return;
                }
                financeDialog = new FinanceDialog(getActivity());
                financeDialog.setTitle(R.string.fragment_my_money_modify_label);
                financeDialog.setPositiveButton(getString(R.string.positive_button_finish_action),
                        (dialog, which) ->
                                presenter.onItemClick(position, financeDialog.getName(), financeDialog.getValue())
                );
                financeDialog.setNegativeButton(R.string.negative_button_cancel_action, null);
                financeDialog.createDialog();
                financeDialog.setName(presenter.getItemAtPosition(position).getNombre());
                financeDialog.setValue(presenter.getItemAtPosition(position).getvalor());
            }

            @Override
            public void onLongClick(View v, int position) {
                if (actionMode != null){
                    v.findViewById(R.id.foto).performClick();
                    updateActionMode();
                    return;
                }
                message.setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.onItemLongClick(position);
                    }
                });
                message.setNegativeButton(R.string.negative_button_label, null);
                message.show();
            }

            @Override
            public void onIconClick(View v, int position) {
                if (actionMode == null){
                    //assert ((AppCompatActivity) getActivity()) != null;
                    actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(MyMoneyFragment.this);
                }
                updateActionMode();
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        list.setLayoutManager(llm);
        list.setHasFixedSize(true);
        list.setAdapter(adapter);
        presenter.requestFinances();
        setHasOptionsMenu(true);

        message = new AlertDialog.Builder(getActivity());
        message.setTitle(R.string.delete_item_confirmation);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_finances, menu);
        menu.findItem(R.id.add).setOnMenuItemClickListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        financeDialog = new FinanceDialog(getActivity());
        financeDialog.setTitle(R.string.fragment_my_money_dialog_title);
        financeDialog.setPositiveButton(getString(R.string.positive_button_finish_action),
                (dialog, which) ->
                        presenter.onPlusIconClick(financeDialog.getName(), financeDialog.getValue())
        );
        financeDialog.setNegativeButton(R.string.negative_button_cancel_action, null);
        financeDialog.createDialog();
        return false;
    }


    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
        MyUtils.changeStatusBarColor(getActivity(), R.color.background_dark);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                //actionModeViewCallbacks.onDeleteActionClicked();
                actionMode.finish();
                return true;
        }
        return false;
    }

    private void updateActionMode(){
        if (adapter.getSelectedCount() == 0){
            //destroy action mode
            actionMode.finish();
            adapter.clearSelections();
            return;
        }
        actionMode.setTitle(getString(R.string.action_mode_selected_items, adapter.getSelectedCount()));
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        adapter.clearSelections();
        this.actionMode = null;
        MyUtils.changeStatusBarColor(getActivity(), R.color.colorPrimaryDark);
    }

    @Override
    public void notifyItemCreated() {
        adapter.notifyItemInserted(adapter.getItemCount());
        presenter.getTotal();
        presenter.getLastUpdate();
        //Toast.makeText(getContext(), "Puto" + presenter.getItemCount() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyChanges() {
        adapter.notifyDataSetChanged();
        presenter.getTotal();
        presenter.getLastUpdate();
    }

    @Override
    public void notifyItemUpdated(int position) {
        adapter.notifyItemChanged(position);
        presenter.getTotal();
        presenter.getLastUpdate();
    }

    @Override
    public void notifyItemDeleted(int position) {
        adapter.notifyItemRemoved(position);
        presenter.getTotal();
        presenter.getLastUpdate();
    }

    @Override
    public void showTotal(int total) {
        totalText.setText(String.format(Locale.US, "$ %d", total));
    }

    @Override
    public void showLastUpdate(String lastUpdate) {
        lastUpdateText.setText(lastUpdate);
    }

}

